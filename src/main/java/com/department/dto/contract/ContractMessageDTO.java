package com.department.dto.contract;

import com.department.entity.Contract;
import com.department.entity.CostCenter;
import com.department.entity.Resource;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

/**
 * <p>
 *  This DTO represents the message the will be sent to the Pub/Sub channel
 *  that in turn will be converted to {@link com.department.entity.Contract}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContractMessageDTO {

    // In this case, represents the Cost Center code.
    private String code;
    // In this case, represents the Resource's email.
    private String email;
    // In this case, the contract document value.
    private String document;
    // The resource position, like Manager, CEO.
    private String position;
    // Boolean that indicates if the contract is active or not.
    private boolean active;

    /**
     * <p>
     *  Converts from this DTO to it's business representation, that in this
     *  case means {@link Contract}
     * @return @see {@link Contract}
     */
    public Contract toBusiness() {
        // First copy all primitive attributes
        Contract entity = new Contract();
        BeanUtils.copyProperties(this, entity);

        // We only need the resource's email
        Resource resource = new Resource();
        resource.setEmail(email);
        entity.setResource(resource);

        // We only need the cost center code
        CostCenter costCenter = new CostCenter();
        costCenter.setCode(code);
        entity.setCostCenter(costCenter);

        return entity;
    }
}
