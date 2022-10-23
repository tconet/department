package com.cesar.sharing.dto.resource;

import com.cesar.sharing.dto.DtoConverter;
import com.cesar.sharing.entity.Resource;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * Simple DTO (Data Transfer Object) that represents the business
 * entity {@link Resource}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResourceDTO extends DtoConverter<ResourceDTO, Resource> {

    private Long id;
    @Schema(name = "name", defaultValue="Jon Doe")
    private String name;
    @Schema(name = "email", defaultValue="examplo@cesar.org.br")
    private String email;
    @Schema(name = "document", defaultValue="98162387480")
    private String document;
    @Schema(name = "status", defaultValue="human")
    private String status;

}
