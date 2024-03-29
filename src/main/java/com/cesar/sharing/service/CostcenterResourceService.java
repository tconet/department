package com.cesar.sharing.service;

import com.cesar.sharing.entity.CostCenter;
import com.cesar.sharing.entity.CostcenterResource;
import com.cesar.sharing.entity.Resource;
import com.cesar.sharing.exceptions.BusinessException;
import com.cesar.sharing.model.SimpleCostCenter;
import com.cesar.sharing.repository.CostcenterResourceRepository;
import com.cesar.sharing.repository.CostCenterRepository;
import com.cesar.sharing.repository.ResourceRepository;
import com.cesar.sharing.types.CostCenterStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * <p>
 *  This class represents the business layer of {@link CostcenterResource}
 *  entity. Here we must ensure all rules specified to control the relationship between
 *  those two entities {@link CostCenter} and {@link Resource}.
 *  Because of the nature of the business, we'll not provide a full CRUD operation
 *  under this entity, all information about it will come from integration.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CostcenterResourceService  {

    private final CostcenterResourceRepository repository;
    private final CostCenterRepository costCenterRepository;
    private final ResourceRepository resourceRepository;

    /**
     * <p>
     *  Search for all relationship between {@link CostCenter} and {@link Resource}
     *  based on the informed parameters.
     *
     * @param email The resource's e-mail (We're using LIKE to search this field)
     * @param code The Cost Center code (We're using LIKE to search this field)
     * @param name The Cost Center name (We're using LIKE to search this field)
     * @param status Must be one of those {@link CostCenterStatus}
     * @param isApprover If the resource is the approver or not.
     * @return A list of {@link SimpleCostCenter}
     */
    public Optional<List> findAllCostCenterByApprover(String email, String code, String name, String status, Boolean isApprover) {
        return repository.findAllCostCenterByApprover(email, code, name, status, isApprover);
    }

    /**
     * <p>
     *  This method is intended to create or update a @see {@link CostcenterResource},
     *  but first we have to ensure some basic business rules, like, we can't have
     *  duplicated relationship, so if already exists, means that we'll just update
     *  the isApprover field, otherwise, we'll create a new one.
     *
     * @param entity @see {@link CostcenterResource}
     */
    @Transactional
    public void createOrUpdate(CostcenterResource entity) {

        // code and email must have value
        validateMandatoryFields(entity);

        // Find the relationship based on email and code
        Optional<CostcenterResource> optional = repository.findOneByResourceEmailAndCostCenterCode(
                entity.getResource().getEmail(), entity.getCostCenter().getCode());

        // Exists?
        if (optional.isEmpty()) {
            // NEW ON CASE: just save...
            fillRelationship(entity);
            repository.save(entity);
            return;
        }
        // UPDATE CASE: just update isApprover field
        CostcenterResource toUpdate = optional.get();
        toUpdate.setApprover(entity.isApprover());
        repository.save(toUpdate);
    }

    /**
     * <p>
     *  Validates if the relationship between the {@link Resource} "Approver" and
     *  the {@link CostCenter} exists.
     * @param approverEmail The approver email
     * @param costCenterCode The {@link CostCenter} code
     */
    public void validateRelationship(String approverEmail, String costCenterCode) {

        // Search based on resource's email and cost center code.
        Optional<CostcenterResource> relationship = repository.findOneByResourceEmailAndCostCenterCode(
                approverEmail, costCenterCode);

        // First check if exists, if true, also check if is approver.
        // If not, throws an exception.
        if ( relationship.isEmpty() || !relationship.get().isApprover() ) {

            // Just build the error message params
            ArrayList<String> param = new ArrayList<>();
            param.add(approverEmail);
            param.add(costCenterCode);
            throw new BusinessException("costCenter.resource.relationship.not.exist", param);
        }
    }

    // Private

    /**
     * <p>
     *  Just validate if the Cost Center code and Resource's e-mail were informed.
     *  If not, an exception will be thrown
     * @param entity @see {@link CostcenterResource}
     * @throws BusinessException Thrown in the case of some mandatory field is empty
     */
    private void validateMandatoryFields(CostcenterResource entity) throws BusinessException {

        if ( !entity.hasCode() )
            throw new BusinessException("field.must.have.value", "code");

        if ( !entity.hasEmail() )
            throw new BusinessException("field.must.have.value", "email");
    }

    /**
     * <p>
     *  The proposal of this method is to fill the relationship fields. Before that,
     *  first we must check if those entities exists. If not, an exception wil be throws
     * @param entity @see {@link CostcenterResource}
     */
    private void fillRelationship(CostcenterResource entity) {

        // First validation: Cost Center must exist by code
        Optional<CostCenter> costCenter = costCenterRepository.findOneByCode(entity.getCostCenter().getCode());
        if ( costCenter.isEmpty() )
            // In this case, throw the exception
            throw new BusinessException("costCenter.not.found.by.code", entity.getCostCenter().getCode());

        // Second validation: Resource must exist by email
        Optional<Resource>  resource = resourceRepository.findOneByEmail(entity.getResource().getEmail());
        if ( resource.isEmpty() )
            // In this case, throw the exception
            throw new BusinessException("resource.not.found.by.email", entity.getResource().getEmail());

        // Just update those entities
        entity.setCostCenter(costCenter.get());
        entity.setResource(resource.get());
    }
}
