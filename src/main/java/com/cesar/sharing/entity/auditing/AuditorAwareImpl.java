package com.cesar.sharing.entity.auditing;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * <p>
 * AuditorAware
 * In case you use either @CreatedBy or @LastModifiedBy, the auditing infrastructure
 * somehow needs to become aware of the current principal. To do so, we provide an
 * AuditorAware<T> SPI interface that you have to implement to tell the infrastructure
 * who the current user or system interacting with the application is. The generic
 * type T defines what type the properties annotated with @CreatedBy or @LastModifiedBy
 * have to be, that in this case, we're using just a simple {@link String}.
 */
@Component
public class AuditorAwareImpl implements AuditorAware<String> {

    /**
     * <p>
     *  Provides the value that will be used to fill the auditable
     *  fields, like @CreatedBy or @LastModifiedBy, that for now we're
     *  using a fixed String, but in the future must must change this
     *  value for the current authenticated user.
     *
     * @return Represents the name of the principal
     */
    @Override
    public Optional<String> getCurrentAuditor() {
        // TODO: When the security context is enabled, then we disable the following code
        // Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // if (authentication == null || !authentication.isAuthenticated()) { return null; }
        // return ((MyUserDetails) authentication.getPrincipal()).getUser().getName(); }
        return Optional.of("System");
    }

}
