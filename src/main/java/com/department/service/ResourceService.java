package com.department.service;

import com.department.entity.Resource;
import com.department.exceptions.BusinessException;
import com.department.repository.ResourceRepository;
import com.department.types.ResourceStatus;
import com.department.utils.FieldValidatorUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
public class ResourceService extends SearchService<Resource> {

    private final ResourceRepository repository;

    /**
     * <p>
     * Default constructor, only exist explicitly to enable the child
     * class provides the correct instance of the desired repository interface.
     * @param repository  Ony implementation of {@link JpaSpecificationExecutor}
     */
    public ResourceService(ResourceRepository repository) {
        super(repository);
        this.repository = repository;
    }

    /**
     * <p>
     *  Creates a new {@link Resource}, but before that, we'll validate some
     *  basics rules. First of all, we must check if all mandatory fields
     *  have been informed, then, we must validate for duplication based on
     *  it's e-mail field. At the end, we just save the new resource.
     * @param entity @see {@link Resource}
     * @return a new @see {@link Resource}
     */
    public Resource create(Resource entity) {

        // Do all necessary validations
        validateBeforeSave(entity);

        // Now check to avoid duplications
        isDuplicated(entity);

        // At the end, save a new resource
        repository.save(entity);
        return entity;
    }

    /**
     * <p>
     * Validates if exists a {@link Resource} with the informed e-mail.
     * @param email Mandatory, resource email.
     * @return true, if exists, otherwise, false.
     */
    public boolean existByEmail(String email) {
        if (Objects.isNull(email))
            throw new BusinessException("field.must.have.value", "email");
        // do the search and check if it has value.
        return !repository.findOneByEmail(email).isEmpty();
    }

    // Private

    /**
     * <p>
     *  Do all necessary validation that must be checked before save a {@link Resource},
     *  First we check if all mandatory field were informed. After that, we check if the
     *  status has one of those expected values based on {@link ResourceStatus}
     *
     * @param entity @see {@link Resource}
     * @throws BusinessException Throws if one of those validation went wrong.
     */
    private void validateBeforeSave(Resource entity) throws BusinessException {

        // Use this utility to validate if all mandatory fields has a value.
        // In this case, the 'id' can be null
        FieldValidatorUtil.validateMandatoryFields(entity, Resource.class, Set.of("id"));

        // Check if the informed status has a valid value.
        if (ResourceStatus.isValid(entity.getStatus()))
            return;

        // In this case, we must throw the exception
        throw new BusinessException("resource.invalid.status", entity.getStatus().toString());
    }

    /**
     * <p>
     *  Validates if we already have an entity with the same email field value.
     *   If true, an exception will be throws. Before that, we must check if
     *   the informed e-mail has a valid format.
     * @param entity @see {@link Resource}
     * @throws BusinessException
     *              throws to inform that already exists a resource
     *              with the same e-mail. We must avoid duplication
     */
    private void isDuplicated(Resource entity) throws BusinessException {

        // First check if the e-mail has a valid format.
        if (!EmailValidator.getInstance().isValid(entity.getEmail()))
            throw new BusinessException("invalid.email.format", entity.getEmail());

        // Now, check if already exists a resource with the same e-mail.
        Optional<Resource> optional = repository.findOneByEmail(entity.getEmail());
        if ( optional.isEmpty() )
            return;

        // We found a duplications, throw the exception.
        throw new BusinessException("resource.already.exists", entity.getEmail());
    }
}
