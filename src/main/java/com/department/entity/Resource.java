package com.department.entity;

import com.department.entity.auditing.Auditable;
import com.department.types.ResourceStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * <p>
 *  Resource (Recurso).
 *  Means something that can be shared.
 */
@Data
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


}
