package com.department.repository;

import com.department.entity.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Long> {

    @Query("SELECT c FROM Contract c WHERE c.costCenter.code = :code AND c.resource.email = :email AND c.document = :document")
    Optional<Contract> findContract(@Param("code") String code, @Param("email") String email, @Param("document") String document);


}
