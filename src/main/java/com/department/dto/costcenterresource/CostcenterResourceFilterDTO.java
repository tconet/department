package com.department.dto.costcenterresource;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 *  This DTO represents all filter options to search for some Cost Center based on
 *  it's relationship with Resource.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CostcenterResourceFilterDTO {

    @Schema(name = "name", defaultValue="SUPORTE")
    private String name;
    @Schema(name = "code", defaultValue="1.2.00.002")
    private String code;
    @Schema(name = "status", defaultValue="open")
    private String status;
    @Schema(name = "email", defaultValue="example@gmail.com")
    private String email;
    @Schema(name = "isApprover", defaultValue="false")
    private boolean isApprover;

}
