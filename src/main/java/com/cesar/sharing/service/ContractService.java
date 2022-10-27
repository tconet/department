package com.cesar.sharing.service;

import com.cesar.sharing.entity.Resource;
import com.cesar.sharing.exceptions.BusinessException;
import com.cesar.sharing.repository.CostCenterRepository;
import com.cesar.sharing.repository.ResourceRepository;
import com.cesar.sharing.entity.Contract;
import com.cesar.sharing.entity.CostCenter;
import com.cesar.sharing.repository.ContractRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class ContractService {

    private final ContractRepository repository;
    private final CostCenterRepository costCenterRepository;
    private final ResourceRepository resourceRepository;

    /**
     * <p>
     *  This method is intended to create or update a @see {@link Contract}
     *  Before save, we must check if the contract already exists based on
     *  cost center code, resource's email and contract's document.
     *  If found means that an update case, that in turn, we just can update
     *  two fields (document and isActive).
     *  For the create a new contract case, before save, we must check if the
     *  resource and the cost center exist, if not, an exception will be thrown
     *
     * @param entity @see {@link Contract}
     */
    @Transactional
    public void createOrUpdate(Contract entity) {

        // Check if already exists, if true, means UPDATE
        Optional<Contract> contract = findContract(entity);
        if ( contract.isPresent() ) {
            // UPDATE CASE...
            contract.get().update(entity);
            repository.save(contract.get());
            return;
        }
        // CREATE CASE...
        // Before create a new onw, we must ensure just one ACTIVE Contract at time.
        markAsInactive(entity);

        // TODO: When a new contract arrives, means that we must
        //       create a new ResourceSharing
        // Now we must fill the relationship fields
        fillRelationship(entity);
        repository.save(entity);
    }

    /**
     * <p>
     *  Try to find an active contract based on the resource's email. If not found,
     *  an exception will be throws, even if we found more then one active contract
     * @param email The Contract resource's email
     * @return @see {@link Contract}
     */
    public Contract findActiveContract(String email) {
        Objects.requireNonNull(email);
        Optional<List<Contract>> contracts = repository.findByResourceEmailAndIsActiveTrue(email);

        // Must exist an active contract
        if (contracts.get().size() == 0)
            throw new BusinessException("contract.not.exist.active.for.resource",
                    email);

        // Can't have more than one active at time.
        if ( contracts.get().size() > 1 )
            throw new BusinessException("contract.more.than.one.active.for.resource",
                    email);

        // Just return the current active contract.
        return contracts.get().get(0);
    }


    // Private

    /**
     * <p>
     *  Set all contract as inactive based for a specific {@link Resource}.
     *  This function is intended to ensure just one active contract at time,
     *  so before create a new one, we must call this function to accomplish
     *  this rule.
     * @param entity @see {@link Contract}
     */
    private void markAsInactive(Contract entity) {
        Optional<List<Contract>> contracts = repository.findByResourceEmailAndIsActiveTrue(entity.getEmail());
        contracts.ifPresent(contractList -> {
            for (Contract toUpdate : contractList)
                toUpdate.setActive(false);
            repository.saveAll(contractList);
        });
    }

    /**
     * <p>
     *  Search for a contract based on some information like, resource's email,
     *  cost center code and the contract document, with all those three fields
     *  together, can't be another contract.
     * @param entity @see {@link Contract}
     * @return null, or a {@link Contract} if found.
     */
    private Optional<Contract> findContract(Contract entity) {
        return repository.findContract(entity.getCode(), entity.getEmail(), entity.getDocument());
    }

    /**
     * <p>
     *  The proposal of this method is to fill the relationship fields for {@link Contract}, but Before that,
     *  first we must check if those entities exists {@link Resource} and
     *  {@link CostCenter}, If not, an exception wil be throws
     * @param entity @see {@link Contract}
     */
    private void fillRelationship(Contract entity) {

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
