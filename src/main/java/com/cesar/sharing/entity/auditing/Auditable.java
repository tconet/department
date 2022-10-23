package com.cesar.sharing.entity.auditing;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * <p>
 * Spring Data provides a great support to keep track of the persistence layer changes.
 * By using auditing, we can store or log the information about the changes on the entity
 * such as who created or changed the entity and when the change is made.
 * MappedSuperclass annotation is used to specify that the class itself is not an entity
 * but its attributes can be mapped in the same way as an entity, however this mappings will
 * apply only to its subclasses. So each class inherits the abstract Auditable class will
 * contain these attributes
 * EntityListeners annotation is used to configure AuditingEntityListener which contains
 * the @PrePersist and @PreUpdate methods in order to capture auditing information, but here
 * we're using another approach, like Provider Auditor, as we'll explain next.
 * createdDate and lastModifiedDate fields are filled according to the current time. Besides,
 * createdBy and lastModifiedBy annotations needs a way to get the user who is performing
 * the action. In order to provide this information we need to implement the AuditorAware interface
 * that we did by {@link AuditorAwareImpl}
 *
 * @param <U> The type to be used on some fields, like (createdBy and lastModifiedBy)
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class Auditable<U> {

    @CreatedBy
    protected U createdBy;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    protected Date creationDate;

    @LastModifiedBy
    protected U lastModifiedBy;

    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    protected Date lastModifiedDate;

}
