package com.cesar.sharing.repository;

import com.cesar.sharing.entity.CostcenterResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CostcenterResourceRepository extends JpaRepository<CostcenterResource, Long>, CustomCostcenterResourceRepository {

    /**
     * <p>
     *  Find a specific relationship based on the informed parameters.
     * @param email The resource's e-mail
     * @param code The Cost Center code
     * @return @see {@link CostcenterResource}
     */
    Optional<CostcenterResource> findOneByResourceEmailAndCostCenterCode(String email, String code);

}
