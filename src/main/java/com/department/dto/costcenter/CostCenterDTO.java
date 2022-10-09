package com.department.dto.costcenter;

import com.department.dto.DtoConverter;
import com.department.entity.CostCenter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * Simple DTO (Data Transfer Object) that represents the business
 * entity {@link CostCenter}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CostCenterDTO extends DtoConverter<CostCenterDTO, CostCenter> {

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

}
