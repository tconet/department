package com.department.entity;

import com.department.entity.auditing.Auditable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Contract extends Auditable<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "resource_id")
    private Resource resource;

    /** Means  the Resource's Cost Center (Origen) */
    @ManyToOne
    @JoinColumn(name = "costcenter_id")
    private CostCenter costCenter;

    /** Resource document, but can by anything, generally will be the internal company registration (Matricula) */
    @Column( nullable = false )
    private String document;

    /** Resource position EX: Manager, CEO, COO, Engineer */
    @Column( nullable = false )
    private String position;

    @Column( columnDefinition = "boolean default true")
    private boolean isActive;


}
