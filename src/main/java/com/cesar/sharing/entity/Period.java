package com.cesar.sharing.entity;

import com.cesar.sharing.exceptions.BusinessException;
import com.cesar.sharing.entity.auditing.Auditable;
import com.cesar.sharing.types.PeriodStatus;
import com.cesar.sharing.utils.FieldValidatorUtil;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@ToString
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Period extends Auditable<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate initDate;
    private LocalDate endDate;
    private LocalTime endTime;

    /** Status, must be one of those values @see {@link PeriodStatus}, default OPENED */
    @Column( nullable = false, columnDefinition = "varchar(255) default 'opened'")
    private String status = PeriodStatus.OPENED.toString();


    /**
     * <p>
     *  Check some rules about the period dates and time. First of all, in the
     *  update process, only the end date and time can be update, so as the first
     *  rule, the end date can't be before today, the end time can't be before now
     *  and as usual, the init date can't be before the end date. If any validation fail,
     *  then an exception will be throws.
     */
    public void applyDateAndTimeRulesToUpdate() {

        // End date can't be before today
        if ( getEndDate().isBefore(LocalDate.now()) )
            throw new BusinessException("period.endDate.before.now");

        // Just for safety, check also with init date is before end date.
        if ( !getInitDate().isBefore(getEndDate()) )
            throw new BusinessException("period.initDate.not.before.endDate");

        // End Time can't be before now.
        if ( getEndTime().isBefore(LocalTime.now()) )
            throw new BusinessException("period.endTime.not.before.now");
    }

    /**
     * <p>
     *  Check if all rules involving initial date and end date are
     *  according with the rules. For now we have only two rules,
     *  the first one is about initial date, that can't be in the
     *  past, and the second check if the initial date is before
     *  end data. If one of those rules fails, an exception will
     *  be throws.
     */
    public void applyDateRulesToOpenPeriod() {

        // Init date can't be before today
        if ( getInitDate().isBefore(LocalDate.now()) )
            throw new BusinessException("period.initDate.before.now");

        // Init date must be before end date
        if ( !getInitDate().isBefore(getEndDate()) )
            throw new BusinessException("period.initDate.not.before.endDate");
    }

    /**
     * <p>
     *  Before create a {@link Period} we must first validate some basic rules.
     *  The first one, all mandatory fields were informed?
     *  Second, Initial date is before end date?
     *  Third, The informed status has a valid value? In this case must be on of
     *  {@link PeriodStatus}.
     *  If one of those validations mentioned above is invalid, then an appropriated
     *  exception will be throws.
     */
    public void validateBeforeCreate() {

        // All field are mandatory, except id.
        FieldValidatorUtil.validateMandatoryFields(this, Period.class, Set.of("id"));

        // Check all validations about date
        applyDateRulesToOpenPeriod();

        // Validate STATUS
        if (!PeriodStatus.isValid(getStatus()))
            throw new BusinessException("period.invalid.status", getStatus());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Period period = (Period) o;
        return id != null && Objects.equals(id, period.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
