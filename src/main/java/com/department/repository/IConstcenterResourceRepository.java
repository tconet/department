package com.department.repository;

import com.department.entity.CostcenterResource;
import com.department.model.SimpleCostCenter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IConstcenterResourceRepository extends JpaRepository<CostcenterResource, Long> {

    /**
     * <p>
     * List all {@link com.department.entity.CostCenter} for a specific {@link com.department.entity.Resource}
     * in this case, based on the informed e-mail.
     * @param email The resource's e-mail
     * @return All Cost Center related with the resource.
     */
    @Query("SELECT new com.department.model.SimpleCostCenter(cr.costCenter.name, cr.costCenter.code, cr.costCenter.branch_name) "
      + "FROM CostcenterResource AS cr WHERE cr.resource.email = :email AND cr.costCenter.status = :status AND cr.isApprover = true "
      + "GROUP BY cr.costCenter.code, cr.costCenter.name, cr.costCenter.branch_name ORDER BY cr.costCenter.code")
    Optional<List<SimpleCostCenter>> findAllCostCenterByApprover(@Param("email") String email, @Param("status") String status);

    /**
     * <p>
     * List all {@link com.department.entity.CostCenter} where the {@link com.department.entity.Resource}
     * is the official approver, based on the informed e-mail.
     * @param email The resource's e-mail
     * @return All Cost Center that the resource is the official approver.
     */
    Optional<List<CostcenterResource>> findByResourceEmailAndIsApproverTrue(String email);

    /**
     * <p>
     *  Find a specific relationship based on the informed parameters.
     * @param email The resource's e-mail
     * @param code The Cost Center code
     * @return @see {@link CostcenterResource}
     */
    Optional<CostcenterResource> findOneByResourceEmailAndCostCenterCode(String email, String code);

}
