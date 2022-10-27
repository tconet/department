package com.cesar.sharing.dto;

import java.util.List;

/**
 * A class that implements this interface can be used to convert
 * entity DTO into it's database entity representation
 * and back again.
 * Note that the X and Y types must be same Java Object.
 * @param <D>  the type of the dto object representation
 * @param <B>  the type of the business entity
 */
public interface IDtoConverter <D,B> {

    void setup();

    /**
     * Converts the value stored in the entity attribute into its
     * dto representation to be returned from some api.
     * @param entity  represents the business entity to be converted
     * @param clazz The DTO entity class type
     * @return  the converted entity, in this case, the DTO representation
     *              of the business entity
     */
    D toDTO(B entity, Class<D> clazz);


    /**
     * Converts the value stored in the dto attribute into its
     * business representation to be stored in the database.
     * @param dto  represents the dto entity to be converted
     * @param clazz The Business entity class type
     * @return  the converted entity, in this case, the Business representation
     *              of the dto object
     */
    B toBusiness(D dto, Class<B> clazz);

    /**
     * Converts the current dto instance into its business
     * representation.
     * @param clazz The Business entity class type
     * @return  the converted entity, in this case, the Business representation
     *              of the dto object
     */
    B toBusiness(Class<B> clazz);

    /**
     * Converts the list of values stored in the dtos attribute into its
     * list of business object representation.
     * @param dtos The list of dto entities to be converted
     * @param clazz The Business entity class type
     * @return The list of Business entities converted.
     */
    List<B> toBusiness(List<D> dtos, Class<B> clazz);

    /**
     * Converts the list of values stored in the entities attribute into its
     * list of data transfer objects representation to be returned from some api.
     * @param entities  represents the list of business entities to be converted
     * @return  the converted entities, in this case, the list of DTOs representation
     *              of the business entity
     */
    List<D> toDTOs(List<B> entities, Class<D> clazz);


}
