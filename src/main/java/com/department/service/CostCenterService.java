package com.department.service;

import com.department.entity.CostCenter;
import com.department.repository.ICostCenterRepository;
import com.department.utils.FieldValidatorUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.Set;

/**
 * <p>
 *  This class represents the business layer of {@link com.department.entity.CostCenter}
 *  entity. Here we must ensure all rules specified to control a Const Center.
 *
 *  Because of the nature of the business, we'll not provide a full CRUD operation
 *  under this entity, all information about it will come from integration.
 */
@Slf4j
@Service
public class CostCenterService extends SearchService<CostCenter> {

    private final ICostCenterRepository repository;

    /**
     * <p>
     * We need this constructor because we're extending the {@link SearchService} default
     * implementation for support a generic search.
     * @param repository In this case, must be an implementation of @see
     * {@link org.springframework.data.jpa.repository.JpaSpecificationExecutor}
     */
    public CostCenterService(ICostCenterRepository repository) {
        super(repository);
        this.repository = repository;
    }

    /**
     * <p>
     * This method is intended to create or update a @see {@link CostCenter}.
     * As main rule, if the field CODE does not exist into our base, means that
     * it's a new one, otherwise, means that we must update all incoming data.
     * @param entity @see {@link CostCenter}
     * @return The @see {@link CostCenter} with all new information's
     */
    @Transactional
    public CostCenter createOrUpdate(CostCenter entity) {

        // Use this utility to validate if all mandatory fields has a value.
        // In this case, the 'id' can be nul
        FieldValidatorUtil.validateMandatoryFields(entity, CostCenter.class, Set.of("id"));

        // Check if the entity already exists based on its code.
        // As we're not the responsible for create this kind of entity,
        // the code is the unique and distributed way for us to identify
        // if a cost center already exist or not.
        Optional<CostCenter> optional = repository.findOneByCode(entity.getCode());
        if ( optional.isEmpty() ) {
            // NEW ONE CASE... we have nothing left to validate.
            return repository.save(entity);
        }
        // UPDATE CASE...
        // In this case, update all new information's and save.
        CostCenter toUpdate = optional.get();
        entity.setId(toUpdate.getId());
        BeanUtils.copyProperties(entity, toUpdate);
        return repository.save(toUpdate);
    }

    // Private

}
