package com.cesar.sharing.service;

import com.cesar.sharing.entity.Period;
import com.cesar.sharing.exceptions.BusinessException;
import com.cesar.sharing.repository.PeriodRepository;
import com.cesar.sharing.types.PeriodStatus;
import com.cesar.sharing.utils.ConvertUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Optional;

@Slf4j
@Service
public class PeriodService extends SearchService<Period> {

    private final PeriodRepository repository;

    /**
     * <p>
     * Default constructor, only exist explicitly to enable the child
     * class provides the correct instance of the desired repository interface.
     * @param repository  Ony implementation of {@link JpaSpecificationExecutor}
     */
    public PeriodService(PeriodRepository repository) {
        super(repository);
        this.repository = repository;
    }

    /**
     * <p>
     * Open a {@link Period}. Before that, we have a set of rules to accomplish.
     * First, we must ensure just one opened period at time. Then, some basic
     * rules, like if the initial date is before end date. If every thing goes
     * right, then we must open the new period and start the process that will
     * clone all resource sharing from last period.
     * @param entity @see {@link Period}
     * @return a new opened {@link Period}
     */
    @Transactional
    public Period open(Period entity) {

        // Dos not matter, must be Opened.
        entity.setStatus(PeriodStatus.OPENED.name());

        // We must ensure only one opened period at time.
        mustsExistJustOneOpened();

        // Validate some rules before save.
        entity.validateBeforeCreate();

        // Avoid date interval conflict
        avoidIntervalConflict(entity);

        // TODO: Start the process to copy all Resource Sharing.
        return repository.save(entity);
    }

    /**
     * <p>
     *  Close the {@link Period}. This action will be applied into
     *  the current opened period. An exception will be throws if does not
     *  exist an open period.
     * @return @see {@link Period}
     */
    public Period close() {
        // TODO: This action can be trigger also by an configured schedule.
        // Must have an opened period to close.
        Period period = hasOpenedPeriod();

        // Close the period.
        period.setStatus(PeriodStatus.CLOSED.name());
        return repository.save(period);
    }

    /**
     * <p>
     *  The updated process is only restricted to the current opened Period.
     *  So, as the first rule, must exists an opened period, otherwise, an exception
     *  will be throws.
     *  In this method, only the end date and time fields can be updated, with that in mind
     *  we must apply all necessary rules about dates and times. @see period.applyDateRulesToUpdatePeriod()
     * @param endDate The {@link Period} end date field.
     * @param endTime The {@link Period} end time field.
     * @return @see {@link Period} updated.
     */
    public Period update(LocalDate endDate, LocalTime endTime) {
        // Must have an opened period to update.
        Period period = hasOpenedPeriod();

        // Check all rules about dates before save.
        period.setEndDate(endDate);
        period.setEndTime(endTime);
        period.applyDateAndTimeRulesToUpdate();
        return repository.save(period);
    }

    public Period getLast() {
        Optional<Period> period = repository.findTopByOrderByIdAsc();
        if (period.isEmpty())
            throw new BusinessException("period.not.exist");
        return period.get();
    }

    // Private

    /**
     * <p>
     *  Check if exists an opened period. If not, an exception will be throws
     * @return @see {@link Period}
     */
    private Period hasOpenedPeriod() {
        Optional<Period> period = repository.findFirstByStatus(PeriodStatus.OPENED.name());
        if (period.isEmpty())
            throw new BusinessException("period.not.opened");
        return period.get();
    }

    /**
     * <p>
     *  Just check if already exists an opened Period. If true,
     *  then we must throws an exception. We must ensure only
     *  one opened period at time.
     */
    private void mustsExistJustOneOpened() {
        Optional<Period> period = repository.findFirstByStatus(PeriodStatus.OPENED.name());
        if ( period.isPresent() )
            throw new BusinessException("period.already.exist.opened");
    }

    /**
     * <p>
     *  This method check for date interval conflict, in other words, we can't
     *  create another period that the dates has an intersection with previous periods
     *  If found some conflict and exception will be throws.
     * @param entity @see {@link Period}
     */
    private void avoidIntervalConflict(Period entity) {
        Optional<Period> period = repository.findFirstByEndDateGreaterThanEqual(entity.getInitDate());
        if ( period.isPresent() ) {
            // Build error message params
            ArrayList<String> param = new ArrayList<>();
            // TODO: In the future we must format the dates based on current locale.
            param.add(ConvertUtils.toBrazilFormat(entity.getInitDate()));
            param.add(ConvertUtils.toBrazilFormat(entity.getEndDate()));
            param.add(ConvertUtils.toBrazilFormat(period.get().getEndDate()));
            throw new BusinessException("period.invalid.date.interval", param);
        }
    }

}
