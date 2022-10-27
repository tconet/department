package com.cesar.sharing.repository;

import com.cesar.sharing.entity.Period;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface PeriodRepository extends JpaRepository<Period, Long>, JpaSpecificationExecutor<Period> {

    /**
     * <p>
     *  Search for a specific Period based on the informed status and return the first one.
     * @param status must be one of {@link com.cesar.sharing.types.PeriodStatus}
     * @return @see {@link Period}
     */
    Optional<Period> findFirstByStatus(String status);

    Optional<Period> findFirstByEndDateGreaterThanEqual(LocalDate date);

    /**
     * <p>
     *  Get the last Period
     * @return @see {@link Period}
     */
    Optional<Period> findTopByOrderByIdAsc();
}
