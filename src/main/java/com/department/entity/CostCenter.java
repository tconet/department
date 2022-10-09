package com.department.entity;


import com.department.types.CostCenterStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * <p>
 *  Cost Center Entity (Centro de Custo).
 *  Means the place where some resources belongs.
 */
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class CostCenter {

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

}
