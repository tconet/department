package com.department.entity;

import com.department.entity.auditing.Auditable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

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

    public String getCode() { return Objects.isNull(costCenter) ? null : costCenter.getCode(); }

    public String getEmail() {
        return Objects.isNull(resource) ? null : resource.getEmail();
    }

    /**
     * <p>
     *  Only two properties can be updated, the first one is 'Position'
     *  and the second 'isActive' the others can't be update because
     *  any change on them means a new contract at all.
     * @param entity @see {@link Contract}
     */
    public void update(Contract entity) {
        this.setActive(entity.isActive());
        this.setPosition(entity.getPosition());
    }

}
