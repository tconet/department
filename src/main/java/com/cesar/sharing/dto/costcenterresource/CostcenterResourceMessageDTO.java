package com.cesar.sharing.dto.costcenterresource;

import com.cesar.sharing.entity.CostCenter;
import com.cesar.sharing.entity.CostcenterResource;
import com.cesar.sharing.entity.Resource;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * Simple DTO (Data Transfer Object) used to represents the expected
 * Json pattern object that came from Pub/Sub queue for messages related
 * to {@link CostcenterResource} entity.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CostcenterResourceMessageDTO {

    // In this case, represents the Cost Center code.
    private String code;
    // In this case, represents the Resource's email.
    private String email;
    // Boolean that indicates if the resource is the Cost Center approver or not.
    private boolean isApprover;

    /**
     * <p>
     *  Creates an instance of {@link CostcenterResource} based on some properties of
     *  this DTO class.
     * @return @see {@link CostcenterResource}
     */
    public CostcenterResource toBusiness() {

        CostcenterResource entity = new CostcenterResource();
        entity.setApprover(isApprover);

        // We only need the resource's email
        Resource resource = new Resource();
        resource.setEmail(email);
        entity.setResource(resource);

        // We only need the cost center code
        CostCenter costCenter = new CostCenter();
        costCenter.setCode(code);
        entity.setCostCenter(costCenter);

        // At the end, returns the entity converted.
        return entity;
    }
}
