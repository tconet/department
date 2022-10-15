package com.department.dto.resource;

import com.department.dto.DtoConverter;
import com.department.entity.Resource;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * Simple DTO (Data Transfer Object) that represents the business
 * entity {@link com.department.entity.Resource}
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
    @Schema(name = "status", defaultValue="Human")
    private String status;

}
