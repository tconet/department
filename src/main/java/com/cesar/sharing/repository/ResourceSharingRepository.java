package com.cesar.sharing.repository;

import com.cesar.sharing.entity.ResourceSharing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResourceSharingRepository extends JpaRepository<ResourceSharing, Long> {

    /**
     * <p>
     *  Calculate the total allocated percent for a specific resource related to a
     *  specific period of time. Actually, this query is based on the active
     *  resource contract.
     * @param periodId Period identification, related to the allocation requested
     * @param contractId Contract identification, related to the resource
     * @return The total allocated percent. If not found, a null will be return
     */
    @Query("SELECT SUM(rs.percent) as sumPercent FROM ResourceSharing rs " +
            "WHERE rs.period.id = :periodId AND rs.contract.id = :contractId AND rs.status <> 'REJECTED'" +
            "GROUP BY rs.period.id, rs.contract.id")
    Optional<Double> allocatedPercent(@Param("periodId") Long periodId, @Param("contractId") Long contractId);

    Optional<ResourceSharing> findOneByContractIdAndPeriodIdAndCostCenterCode(Long contractId, Long periodId, String costCenterCode);
}
