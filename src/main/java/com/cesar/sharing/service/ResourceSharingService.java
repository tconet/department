package com.cesar.sharing.service;

import com.cesar.sharing.entity.Contract;
import com.cesar.sharing.entity.CostCenter;
import com.cesar.sharing.entity.Period;
import com.cesar.sharing.entity.ResourceSharing;
import com.cesar.sharing.exceptions.BusinessException;
import com.cesar.sharing.repository.ResourceSharingRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class ResourceSharingService {

    private final ResourceSharingRepository repository;
    private final PeriodService periodService;
    private final ContractService contractService;
    private final CostcenterResourceService costcenterResourceService;
    private final CostCenterService costCenterService;

    /**
     * <P>
     * Creates all sharing for a specific resource. Before that, we must check a lot
     * of business rules.
     * 1. Must exist a current period. Always work with the last period. TODO: for MANAGER must be the opened period
     * 2. Must exist an active contract for the resource that will be shared.
     * 3. Prepare the sharing requests: Check the "prepare" method on this class for more details.
     * 4. Calculate if the sharing percent does not overflow the available.
     * 5. Set the sharing status, must be OPENED, the contract and period.
     *
     * @param entities List of {@link com.cesar.sharing.entity.Resource} to share
     * @return The list of {@link ResourceSharing}
     */
    @Transactional
    public List<ResourceSharing> share(List<ResourceSharing> entities) {

        // 1. Always work with the last period.
        Period period = periodService.getLast();

        // 2. Get the active contract based on resource's email. Must exist an active contract,
        // otherwise an exception will be throws. The Origin Cost Center will be based on this contract.
        Contract contract = contractService.findActiveContract(getResourceEmail(entities));

        // 3. Here in this stage we do a lot of rules and validations,
        // check the method description for more information.
        prepare(entities, period, contract);

        // 4. Check for overflow.
        checkOverflowPercent(period, contract, entities);

        // At the end, saves all resources sharing
        return repository.saveAll(entities);
    }

    // Private

    /**
     * <p>
     *  This method prepare the list of entities ({@link ResourceSharing}) before create
     *  all sharing requested. To accomplish this task, first we're going to loop over
     *  each sharing and check if all information are valid, like if the approvers informed
     *  are related with their respective Cost Center, both (Origin and Destini).
     *  We also check if the sharing already exists, if true, than we must reset the percent value,
     *  we'll being considering the new value sent.
     *  As we're looping over this sharing, we also use the opportunity to set the Period
     *  and the Contract for all entities.
     *
     * @param entities List of {@link ResourceSharing}
     * @param period @see {@link Period}
     * @param contract @see {@link Contract}
     */
    @Transactional
    private void prepare(List<ResourceSharing> entities, Period period, Contract contract) {

        // The list of sharing to reset.
        List<ResourceSharing> toReset = new ArrayList<>();
        for (ResourceSharing entity: entities) {

            // Apply all rules related with Cost Center.
            validateCostCenterRules(contract, entity);

            // Search for the sharing
            Optional<ResourceSharing> sharing = repository.
                    findOneByContractIdAndPeriodIdAndCostCenterCode(
                            contract.getId(), period.getId(), entity.getCostCenterCode());

            // If exists, save for reset in batch!
            sharing.ifPresent(toReset::add);

            // Update the period and Contract.
            entity.setPeriod(period);
            entity.setContract(contract);
        }
        // Has sharing? then, reset!
        if (!toReset.isEmpty())
            repository.deleteAll(toReset);
    }

    /**
     * <p>
     *  Validates all rules related with {@link CostCenter}.
     *  1. Destini Cost Center can't equals to Destini
     *  2. Validate the Approver
     *  3. Validate the branch rule
     * @param contract @see {@link Contract}
     * @param entity @see {@link ResourceSharing}
     */
    private void validateCostCenterRules(Contract contract, ResourceSharing entity) {

        // Origin is the same as Destini? if true, throws an exception.
        if ( entity.getCostCenterCode().equals(contract.getCode()) )
            throw new BusinessException("resource.sharing.same.cost.center.origin");

        // Validate the approvers (Destini and Origin)
        validateRelationship(contract, entity);

        // Validate branch rules
        validateBranchRule(contract, entity);
    }

    /**
     * <p>
     *  Here we check if those relationships are valid.
     *  1. Cost Center (Destini) x Resource (Must be an approver for this cost center)
     *  2. Cost Center (Origin) x Resource (Must be an approver for this cost center)
     *  Both relationship must exist, otherwise an exception will be throws.
     * @param contract @see {@link Contract}
     * @param entity @see {@link ResourceSharing}
     */
    private void validateRelationship(Contract contract, ResourceSharing entity) {
        // Validate Relationship Cost Center (Destini) x Resource (Approver of Destini)
        costcenterResourceService.validateRelationship(entity.getDestiniApproverEmail(), entity.getCostCenterCode());
        // Validate Relationship Cost Center (Origin) x Resource (Approver of Origin)
        costcenterResourceService.validateRelationship(entity.getOriginApproverEmail(), contract.getCode());
    }

    /**
     * <p>
     *  As the main rule, we can't share a resource between Const Center with
     *  different branch. The contract contains the resource's Cost Center Origin
     *  and this one must be in the same branch when compared with the Cost Center Destini
     *  if not, an exception will be throws.
     * @param contract @see {@link Contract}
     * @param entity @see {@link ResourceSharing}
     */
    private void validateBranchRule(Contract contract, ResourceSharing entity) {

        CostCenter costCenter = costCenterService.findByCode(entity.getCostCenterCode());
        if (!costCenter.hasSameBranch(contract.getCostCenter())) {
            ArrayList<String> param = new ArrayList<>();
            param.add(contract.getCostCenter().getBranch_name());
            param.add(costCenter.getBranch_name());
            throw new BusinessException("resource.sharing.different.branch", param);
        }
    }

    /**
     * <p>
     *  Validates if the requested allocation percent does not overflow the available percent.
     * @param period @see {@link Period} The current period for sharing the resource
     * @param contract @see {@link Contract} The resource contract
     * @param entities @see The list of sharing
     */
    private void checkOverflowPercent(Period period, Contract contract, List<ResourceSharing> entities) {

        // Get the current allocated percent
        Double allocatedPercent = getCurrentAllocatedPercent(period, contract);

        // Get the total requested allocation percent from the list of sharing
        Double requestedAllocationPercent = getTotalRequestedPercent(entities);

        // We must ensure that the total allocated percent don't overflow 100%
        // Check if the sum is bigger than 100%, if true, throws an exception.
        if ( Double.sum(allocatedPercent, requestedAllocationPercent) > 100 ) {
            ArrayList<String> param = new ArrayList<>();
            double remainPercent = Double.sum(100, -allocatedPercent);
            param.add(Double.toString(remainPercent));
            param.add(Double.toString(requestedAllocationPercent));
            throw new BusinessException("resource.sharing.percent.overflow", param);
        }
    }

    /**
     * <p>
     *  Get the total allocated percent for a specific period and contract.
     * @param period @see {@link Period}
     * @param contract @see {@link Contract}
     * @return The total allocated percent.
     */
    private Double getCurrentAllocatedPercent(Period period, Contract contract) {
        // Get the total allocated percent for a specific resource
        Optional<Double> totalPercent = repository.allocatedPercent(period.getId(), contract.getId());

        // If null, means that does not have any allocation yet, so set to zero.
        return totalPercent.isEmpty() ? 0d : totalPercent.get();
    }

    /**
     * <p>
     *  Get the total sharing requested percent.
     * @param entities List of {@link ResourceSharing}
     * @return Double the total requested sharing value
     */
    private Double getTotalRequestedPercent(List<ResourceSharing> entities) {

        Double totalPercent = 0d;
        for (ResourceSharing sharing: entities) {
            totalPercent += sharing.getPercent();
        }
        return totalPercent;
    }

    /**
     * <p>
     *  This method is intended to get the resource's email. Before that, we must
     *  ensure that the list of sharing is not empty.
     * @param entities List of {@link ResourceSharing}
     * @return the Resource's email
     */
    private String getResourceEmail(List<ResourceSharing> entities) {

        if ( entities.isEmpty() )
            throw new BusinessException("resource.sharing.must.be.informed");

        for (ResourceSharing sharing: entities) {
            return sharing.getEmail();
        }
        throw new BusinessException("resource.sharing.must.be.informed");
    }
}
