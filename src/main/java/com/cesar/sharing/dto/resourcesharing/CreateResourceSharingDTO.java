package com.cesar.sharing.dto.resourcesharing;

import com.cesar.sharing.dto.DtoConverter;
import com.cesar.sharing.entity.ResourceSharing;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Simple DTO (Data Transfer Object) that represents the business
 * entity {@link ResourceSharing}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateResourceSharingDTO extends DtoConverter<CreateResourceSharingDTO, ResourceSharing> {

    @NotNull(message = "resource.sharing.field.email.mandatory")
    @Schema(name = "email", defaultValue="examplo@cesar.org.br")
    private String email;

    @NotNull(message = "resource.sharing.field.costCenter.code.mandatory")
    @Schema(name = "costCenterCode", defaultValue="1.2.00.002")
    private String costCenterCode;

    @NotNull(message = "resource.sharing.field.origin.approver.email.mandatory")
    @Schema(name = "originApproverEmail", defaultValue="examplo@cesar.org.br")
    private String originApproverEmail;

    @NotNull(message = "resource.sharing.field.destini.approver.email.mandatory")
    @Schema(name = "destiniApproverEmail", defaultValue="examplo@cesar.org.br")
    private String destiniApproverEmail;

    @NotNull(message = "resource.sharing.field.percent.mandatory")
    @Schema(name = "percent", defaultValue="20.5")
    private Double percent;

    @Schema(name = "observation", defaultValue="Any observation you want")
    private String observation;


    public List<ResourceSharing> toBusiness(List<CreateResourceSharingDTO> dtos) {
        List<ResourceSharing> entities = new ArrayList<>();
        for (CreateResourceSharingDTO dto: dtos) {
            ResourceSharing entity = toBusiness(dto, ResourceSharing.class);
            entity.setContractByEmail(dto.getEmail());
            entities.add(entity);
        }
        return entities;
    }

}
