package com.department.repository;

import com.department.entity.Period;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface PeriodRepository extends JpaRepository<Period, Long>, JpaSpecificationExecutor<Period> {

    Optional<Period> findFirstByStatus(String status);

    Optional<Period> findFirstByEndDateGreaterThanEqual(LocalDate date);
}
