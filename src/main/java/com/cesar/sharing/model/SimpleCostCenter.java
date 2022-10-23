package com.cesar.sharing.model;

import com.cesar.sharing.entity.CostcenterResource;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 *  This model represents the result object for a generic search about the
 *  relationship represented by {@link CostcenterResource}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SimpleCostCenter {

    @Schema(name = "name", defaultValue="SUPORTE")
    private String name;
    @Schema(name = "code", defaultValue="1.2.00.002")
    private String code;
    @Schema(name = "branch_name", defaultValue="RECIFE")
    private String branch_name;

}
