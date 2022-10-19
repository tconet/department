package com.department.repository;

import com.department.entity.CostcenterResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConstcenterResourceRepository extends JpaRepository<CostcenterResource, Long>, CustomCostcenterResourceRepository {

    /**
     * <p>
     *  Find a specific relationship based on the informed parameters.
     * @param email The resource's e-mail
     * @param code The Cost Center code
     * @return @see {@link CostcenterResource}
     */
    Optional<CostcenterResource> findOneByResourceEmailAndCostCenterCode(String email, String code);

}
