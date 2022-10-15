package com.department.entity;

import com.department.entity.auditing.Auditable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

/**
 * <p>
 * This entity is a composition relationship strategy between
 * {@link Resource} and {@link CostCenter} entities, we also have
 * some extra information, like a field the indicates if the
 * resource is or not the approval of a specific Cost Center.
 */
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class CostcenterResource extends Auditable<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "resource_id")
    private Resource resource;

    @ManyToOne
    @JoinColumn(name = "costcenter_id")
    private CostCenter costCenter;

    @Column( columnDefinition = "boolean default false")
    private boolean isApprover;

    public boolean hasCode() {
        if (Objects.isNull(costCenter) || Objects.isNull(costCenter.getCode()))
            return false;
        return !costCenter.getCode().isEmpty();
    }
    public boolean hasEmail() {
        if (Objects.isNull(resource) || Objects.isNull(resource.getEmail()))
            return false;
        return !resource.getEmail().isEmpty();
    }
}
