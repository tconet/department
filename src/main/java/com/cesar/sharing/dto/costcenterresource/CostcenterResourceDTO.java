package com.cesar.sharing.dto.costcenterresource;

import com.cesar.sharing.dto.resource.ResourceDTO;
import com.cesar.sharing.entity.CostcenterResource;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * <p>
 * Simple DTO (Data Transfer Object) that represents the business
 * entity {@link CostcenterResource}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CostcenterResourceDTO {

    private Long id;
    @Schema(name = "name", defaultValue="SUPORTE")
    private String name;
    @Schema(name = "code", defaultValue="1.2.00.002")
    private String code;
    @Schema(name = "status", defaultValue="Open")
    private String status;
    @Schema(name = "branch_code", defaultValue="0002")
    private String branch_code;
    @Schema(name = "branch_name", defaultValue="SÃO PAULO")
    private String branch_name;

    @Schema(name = "isApprover", defaultValue="false")
    private boolean isApprover;

    @Schema(name = "resources")
    private List<ResourceDTO> resources;
}
