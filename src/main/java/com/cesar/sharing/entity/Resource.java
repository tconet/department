package com.cesar.sharing.entity;

import com.cesar.sharing.types.ResourceStatus;
import com.cesar.sharing.entity.auditing.Auditable;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

/**
 * <p>
 *  Resource (Recurso).
 *  Means something that can be shared.
 */
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Resource extends Auditable<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Resource name, no rules, just can't be null */
    @Column( nullable = false )
    private String name;

    /** Resource email, not null and must be unique */
    @Column( nullable = false, unique = true )
    private String email;

    /** Resource document, can by anything (RG,CNPJ) */
    @Column( nullable = false )
    private String document;

    /** Status, must be one of those values @see {@link ResourceStatus}, default HUMAN */
    @Column( nullable = false, columnDefinition = "varchar(255) default 'human'")
    private String status = ResourceStatus.HUMAN.toString();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Resource resource = (Resource) o;
        return id != null && Objects.equals(id, resource.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
