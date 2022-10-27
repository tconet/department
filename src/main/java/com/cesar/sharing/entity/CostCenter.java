package com.cesar.sharing.entity;


import com.cesar.sharing.entity.auditing.Auditable;
import com.cesar.sharing.types.CostCenterStatus;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

/**
 * <p>
 *  Cost Center Entity (Centro de Custo).
 *  Means the place where some resources belongs.
 */
@Getter
@Setter
@ToString
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class CostCenter extends Auditable<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Cost Center name, no rules, just can't be null */
    @Column( nullable = false )
    private String name;

    /** Example how then plan to use: (1.0.00.001) but can be anything */
    @Column( unique = true, nullable = false)
    private String code;

    /** Status, must be (OPEN or CLOSED) @see {@link CostCenterStatus}, default OPEN */
    @Column( nullable = false, columnDefinition = "varchar(255) default 'Open'")
    private String status = CostCenterStatus.OPEN.toString();

    /** Branch node and name, for now, we don't have an entity to represent those values */
    @Column( nullable = false )
    private String branch_code;
    @Column( nullable = false )
    private String branch_name;

    /**
     * <p>
     *  Validates if both Cost Center have the same branch.
     * @param costCenter @see {@link CostCenter}
     * @return true if the branch is equals, otherwise, false.
     */
    public boolean hasSameBranch(CostCenter costCenter) {
        Objects.requireNonNull(costCenter);
        Objects.requireNonNull(costCenter.getBranch_code());
        return costCenter.getBranch_code().equals(this.branch_code);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        CostCenter that = (CostCenter) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
