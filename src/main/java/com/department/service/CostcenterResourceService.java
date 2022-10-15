package com.department.service;

import com.department.entity.CostCenter;
import com.department.entity.CostcenterResource;
import com.department.entity.Resource;
import com.department.exceptions.BusinessException;
import com.department.model.SimpleCostCenter;
import com.department.repository.IConstcenterResourceRepository;
import com.department.repository.ICostCenterRepository;
import com.department.repository.IResourceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

/**
 * <p>
 *  This class represents the business layer of {@link com.department.entity.CostcenterResource}
 *  entity. Here we must ensure all rules specified to control the relationship between
 *  those two entities {@link com.department.entity.CostCenter} and {@link com.department.entity.Resource}.
 *
 *  Because of the nature of the business, we'll not provide a full CRUD operation
 *  under this entity, all information about it will come from integration.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CostcenterResourceService  {

    private final IConstcenterResourceRepository repository;
    private final ICostCenterRepository costCenterRepository;
    private final IResourceRepository resourceRepository;

    /**
     * <p>
     *  This method is intended to create or update a @see {@link CostcenterResource},
     *  but first we have to ensure some basic business rules, like, we can't have
     *  duplicated relationship, so if already exists, means that we'll just update
     *  the isApprover field, otherwise, we'll create a new one.
     *
     * @param entity @see {@link CostcenterResource}
     * @return The @see {@link CostcenterResource} with all new information's
     */
    @Transactional
    public CostcenterResource createOrUpdate(CostcenterResource entity) {

        // code and email must have value
        validateMandatoryFields(entity);

        // Find the relationship based on email and code
        Optional<CostcenterResource> optional = repository.findOneByResourceEmailAndCostCenterCode(
                entity.getResource().getEmail(), entity.getCostCenter().getCode());

        // Exists?
        if (optional.isEmpty()) {
            // NEW ON CASE: just save...
            fillRelationship(entity);
            return repository.save(entity);
        }
        // UPDATE CASE: just update isApprover field
        CostcenterResource toUpdate = optional.get();
        toUpdate.setApprover(entity.isApprover());
        return repository.save(toUpdate);
    }

    /**
     * <p>
     *  Search for all relationship between {@link CostCenter} and {@link Resource}
     *  based on the informed approver's e-mail.
     * @param email The resource email
     * @return A list of {@link CostcenterResource}
     */
    public Optional<List<SimpleCostCenter>> findAllCostCenterByApprover(String email, String status) {
        return repository.findAllCostCenterByApprover(email, status);
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
            throw new BusinessException("costcenter.not.found.by.code", entity.getCostCenter().getCode());

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
