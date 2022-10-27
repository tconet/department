package com.cesar.sharing.repository;

import com.cesar.sharing.entity.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Long> {

    @Query("SELECT c FROM Contract c WHERE c.costCenter.code = :code AND c.resource.email = :email AND c.document = :document")
    Optional<Contract> findContract(@Param("code") String code, @Param("email") String email, @Param("document") String document);

    /**
     * <p>
     * Search for all active contracts for a specific resource.
     * @param email The {@link com.cesar.sharing.entity.Resource} email
     * @return @see Optional<List<{@link Contract}>>
     */
    Optional<List<Contract>> findByResourceEmailAndIsActiveTrue(String email);



}
