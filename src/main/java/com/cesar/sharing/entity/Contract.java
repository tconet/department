package com.cesar.sharing.entity;

import com.cesar.sharing.entity.auditing.Auditable;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@ToString
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

    /** Resource document, but can be anything, generally will be the internal company registration (Matricula) */
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Contract contract = (Contract) o;
        return id != null && Objects.equals(id, contract.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
