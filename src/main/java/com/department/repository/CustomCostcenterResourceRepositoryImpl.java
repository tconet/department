package com.department.repository;

import com.department.model.SimpleCostCenter;
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
     *  This method is intended to be some kind of generic search to find some {@link com.department.entity.CostCenter}
     *  based on it's relationship with {@link com.department.entity.Resource}. It's possible to omit some fields on
     *  parameter, in this case, we'll not consider those values to accomplish the search.
     *
     *  In this implementation, we're using the {@link EntityManager} to build a generic search through
     *  composition interface. We're also creating a result transformer to summarize the result based
     *  a specific model {@link SimpleCostCenter}
     *
     * @param email The resource's e-mail (We're using LIKE to search this field)
     * @param code The Cost Center code (We're using LIKE to search this field)
     * @param name The Cost Center name (We're using LIKE to search this field)
     * @param status Must be one of those {@link com.department.types.CostCenterStatus}
     * @param isApprover If the resource is the approver or not.
     * @return A list of {@link SimpleCostCenter}
     */
    @Override
    public Optional<List<SimpleCostCenter>> findAllCostCenterByApprover(String email, String code, String name, String status, Boolean isApprover) {

        // Get the session from entity manager.
        Session session = entityManager.unwrap(Session.class);

        // SELECT section, build the result transformed based on SimpleCostCenter model.
        StringBuilder select = new StringBuilder();
        select.append("SELECT new com.department.model.SimpleCostCenter(cr.costCenter.name, cr.costCenter.code, cr.costCenter.branch_name)");

        // FROM section
        StringBuilder from = new StringBuilder();
        from.append(" FROM CostcenterResource AS cr WHERE cr.isApprover = " + isApprover.toString());

        // AND options.
        if (!Objects.isNull(email) && !email.trim().isEmpty())
            from.append(" AND cr.resource.email LIKE " + StringUtils.quote(email+"%"));
        if (!Objects.isNull(code) && !code.trim().isEmpty())
            from.append(" AND cr.costCenter.code LIKE " + StringUtils.quote(code+"%"));
        if (!Objects.isNull(name) && !name.trim().isEmpty())
            from.append(" AND cr.costCenter.name LIKE " + StringUtils.quote(name+"%"));
        if (!Objects.isNull(status) && !status.trim().isEmpty())
            from.append(" AND cr.costCenter.status = " + StringUtils.quote(status) );

        // GROUP BY section.
        select.append(from);
        String groupBy = " GROUP BY cr.costCenter.code, cr.costCenter.name, cr.costCenter.branch_name, cr.costCenter.status, cr.isApprover, cr.resource.email ORDER BY cr.costCenter.name";
        select.append(groupBy);

        // Put all together and execute the query.
        Query<SimpleCostCenter> query = session.createQuery(select.toString());
        List<SimpleCostCenter> results = query.list();
        return Optional.of(results);
    }
}
