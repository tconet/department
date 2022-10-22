package com.department.service;

import com.department.entity.Contract;
import com.department.entity.CostCenter;
import com.department.entity.Resource;
import com.department.exceptions.BusinessException;
import com.department.repository.ContractRepository;
import com.department.repository.CostCenterRepository;
import com.department.repository.ResourceRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
     *
     *  If found means that an update case, that in turn, we just can update
     *  two fields (document and isActive).
     *
     *  For the create a new contract case, before save, we must check if the
     *  resource and the cost center exist, if not, an exception will be thrown
     *
     * @param entity @see {@link Contract}
     * @return @see {@link Contract}
     */
    public Contract createOrUpdate(Contract entity) {

        // Check if already exists, if true, means UPDATE
        Optional<Contract> contract = findContract(entity);
        if ( contract.isPresent() ) {
            // UPDATE CASE...
            contract.get().update(entity);
            return repository.save(contract.get());
        }
        // CREATE CASE...
        // So, first we must fill the relationship fields
        // TODO: When a new contract arrives, means that we must
        //       create a new ResourceSharing
        fillRelationship(entity);
        return repository.save(entity);
    }

    // Private

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
     *  first we must check if those entities exists {@link com.department.entity.Resource} and
     *  {@link com.department.entity.CostCenter}, If not, an exception wil be throws
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
