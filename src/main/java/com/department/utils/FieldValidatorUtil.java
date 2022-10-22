package com.department.utils;

import com.department.exceptions.BusinessException;

import java.lang.reflect.Field;
import java.util.Objects;
import java.util.Set;

public class FieldValidatorUtil {

    /**
     * <p>
     *  This is just an Overhead for validateMandatoryFields() used when all
     *  fields of a specific object is mandatory, so in this case, you don't
     *  need to provide a list of exceptions to not consider.
     *
     * @param entity Can be any kind of {@link Object}
     * @param toClazz Must be the class of the entity parameter
     */
    public static void validateMandatoryFields(Object entity, Class toClazz) {
        validateMandatoryFields(entity, toClazz, Set.of(""));
    }

    /**
     * <p>
     *  This utility function uses java reflection to introspect all fields
     *  into the informed object and search for those one with null value.
     *
     *  It's also possible to inform a list of fields that we must not consider,
     *  in other words, those one that can be null.
     *
     * @param entity Can be any kind of {@link Object}
     * @param toClazz Must be the class of the entity parameter
     * @param notMandatoryFields The list of field that we must not consider, in other words,
     *                           those one that can be null
     */
    public static void validateMandatoryFields(Object entity, Class toClazz, Set<String> notMandatoryFields) {

        Field[] fields = toClazz.getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);

            if ( notMandatoryFields.contains(field.getName()) )
                continue;

            try {
                Object value = field.get(entity);
                if ( Objects.isNull(value) ) {
                    throw new BusinessException("field.must.have.value", field.getName());
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

