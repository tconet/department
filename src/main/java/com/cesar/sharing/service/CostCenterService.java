package com.cesar.sharing.service;

import com.cesar.sharing.exceptions.BusinessException;
import com.cesar.sharing.repository.CostCenterRepository;
import com.cesar.sharing.utils.FieldValidatorUtil;
import com.cesar.sharing.entity.CostCenter;
import com.cesar.sharing.types.CostCenterStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.Set;

/**
 * <p>
 *  This class represents the business layer of {@link CostCenter}
 *  entity. Here we must ensure all rules specified to control a Const Center.
 *  Because of the nature of the business, we'll not provide a full CRUD operation
 *  under this entity, all information about it will come from integration.
 */
@Slf4j
@Service
public class CostCenterService extends SearchService<CostCenter> {

    private final CostCenterRepository repository;

    /**
     * <p>
     * We need this constructor because we're extending the {@link SearchService} default
     * implementation for support a generic search.
     * @param repository In this case, must be an implementation of @see
     * {@link org.springframework.data.jpa.repository.JpaSpecificationExecutor}
     */
    public CostCenterService(CostCenterRepository repository) {
        super(repository);
        this.repository = repository;
    }

    /**
     * <p>
     * This method is intended to create or update a @see {@link CostCenter}.
     * As main rule, if the field CODE does not exist into our base, means that
     * it's a new one, otherwise, means that we must update all incoming data.
     * @param entity @see {@link CostCenter}
     */
    @Transactional
    public void createOrUpdate(CostCenter entity) {

        // Apply all necessary validations before save.
        validateBeforeSave(entity);

        // Check if the entity already exists based on its code.
        // As we're not the responsible for create this kind of entity,
        // the code is the unique and distributed way for us to identify
        // if a cost center already exist or not.
        Optional<CostCenter> optional = repository.findOneByCode(entity.getCode());
        if ( optional.isEmpty() ) {
            // NEW ONE CASE... we have nothing left to validate.
            repository.save(entity);
            return;
        }
        // UPDATE CASE...
        // In this case, update all new information's and save.
        CostCenter toUpdate = optional.get();
        BeanUtils.copyProperties(entity, toUpdate, "id");
        repository.save(toUpdate);
    }

    /**
     * <p>
     *  Search for a specific {@link CostCenter} based on the informed code.
     *  If does not exist, an exception will be throws.
     * @param code The cost center code
     * @return @see {@link CostCenter}
     */
    public CostCenter findByCode(String code) {

        Optional<CostCenter> optional = repository.findOneByCode(code);
        if (optional.isEmpty())
            throw new BusinessException("costCenter.not.found.by.code", code);

        return  optional.get();
    }

    // Private

    /**
     * <p>
     *  Do all necessary validation that must be checked before save a {@link CostCenter}
     *  First we check if all mandatory field were informed. After that, we check if the
     *  status has one of those expected values based on {@link CostCenterStatus}
     *
     * @param entity @see {@link CostCenter}
     * @throws BusinessException Throws if one of those validation went wrong.
     */
    private void validateBeforeSave(CostCenter entity) throws BusinessException {

        // Use this utility to validate if all mandatory fields has a value.
        // In this case, the 'id' can be null
        FieldValidatorUtil.validateMandatoryFields(entity, CostCenter.class, Set.of("id"));

        // Check if the informed status has a valid value.
        if (!CostCenterStatus.isValid(entity.getStatus())) {
            throw new BusinessException("costCenter.invalid.status", entity.getStatus());
        }
    }

}
