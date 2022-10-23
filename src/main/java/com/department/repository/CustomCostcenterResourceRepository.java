package com.department.repository;

import com.department.model.SimpleCostCenter;

import java.util.List;
import java.util.Optional;

/**
 * <p>
 *  Composite repository interface, we aggregate more power to {@link CostcenterResourceRepository}
 */
public interface CustomCostcenterResourceRepository {

    /**
     * <p>
     *  This method is intended to be some kind of generic search to find some {@link com.department.entity.CostCenter}
     *  based on it's relationship with {@link com.department.entity.Resource}. It's possible to omit some fields on
     *  parameter, in this case, we'll not consider those values to accomplish the search.
     *
     * @param email The resource's e-mail (We're using LIKE to search this field)
     * @param code The Cost Center code (We're using LIKE to search this field)
     * @param name The Cost Center name (We're using LIKE to search this field)
     * @param status Must be one of those {@link com.department.types.CostCenterStatus}
     * @param isApprover If the resource is the approver or not.
     * @return A list of {@link SimpleCostCenter}
     */
     Optional<List> findAllCostCenterByApprover(String email, String code, String name, String status, Boolean isApprover);
}
