package com.cesar.sharing.repository;

import com.cesar.sharing.entity.CostCenter;
import com.cesar.sharing.entity.Resource;
import com.cesar.sharing.types.CostCenterStatus;
import com.cesar.sharing.model.SimpleCostCenter;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * <p>
 *  Custom repository interface implementation, here we aggregate more power to the original repository.
 *  In this case, we're implementing some search that the default JPA Repository does not provides.
 */
public class CustomCostcenterResourceRepositoryImpl implements CustomCostcenterResourceRepository {

    @Autowired
    private EntityManager entityManager;

    /**
     * <p>
     *  This method is intended to be some kind of generic search to find some {@link CostCenter}
     *  based on it's relationship with {@link Resource}. It's possible to omit some fields on
     *  parameter, in this case, we'll not consider those values to accomplish the search.
     *  In this implementation, we're using the {@link EntityManager} to build a generic search through
     *  composition interface. We're also creating a result transformer to summarize the result based
     *  a specific model {@link SimpleCostCenter}
     *
     * @param email The resource's e-mail (We're using LIKE to search this field)
     * @param code The Cost Center code (We're using LIKE to search this field)
     * @param name The Cost Center name (We're using LIKE to search this field)
     * @param status Must be one of those {@link CostCenterStatus}
     * @param isApprover If the resource is the approver or not.
     * @return A list of {@link SimpleCostCenter}
     */
    @Override
    public Optional<List> findAllCostCenterByApprover(String email, String code, String name, String status, Boolean isApprover) {

        // Get the session from entity manager.
        Session session = entityManager.unwrap(Session.class);

        // SELECT section, build the result transformed based on SimpleCostCenter model.
        StringBuilder select = new StringBuilder();
        select.append("SELECT new com.cesar.sharing.model.SimpleCostCenter(cr.costCenter.name, cr.costCenter.code, cr.costCenter.branch_name)");

        // FROM section
        StringBuilder from = new StringBuilder();
        from.append(" FROM CostcenterResource AS cr WHERE cr.isApprover = ").append(isApprover.toString());

        // AND options.
        if (!Objects.isNull(email) && !email.trim().isEmpty())
            from.append(" AND cr.resource.email LIKE ").append(StringUtils.quote(email + "%"));
        if (!Objects.isNull(code) && !code.trim().isEmpty())
            from.append(" AND cr.costCenter.code LIKE ").append(StringUtils.quote(code + "%"));
        if (!Objects.isNull(name) && !name.trim().isEmpty())
            from.append(" AND cr.costCenter.name LIKE ").append(StringUtils.quote(name + "%"));
        if (!Objects.isNull(status) && !status.trim().isEmpty())
            from.append(" AND cr.costCenter.status = ").append(StringUtils.quote(status));

        // GROUP BY section.
        select.append(from);
        String groupBy = " GROUP BY cr.costCenter.code, cr.costCenter.name, cr.costCenter.branch_name, cr.costCenter.status, cr.isApprover, cr.resource.email";
        select.append(groupBy);

        // ORDER BY section.
        String orderBy = " ORDER BY cr.costCenter.name";
        select.append(orderBy);

        // Put all together and execute the query.
        Query query = session.createQuery(select.toString());
        // TODO: to implement pagination, we must add the following
        //  .setFirstResult(0)
        //  .setMaxResults(10)
        //  .getResultList();
        //  And also pass the Pageable object into method parameter
        //  @see SearchSpecification for pagination example
        List results = query.list();
        return Optional.of(results);
    }
}
