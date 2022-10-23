package com.cesar.sharing.types;

import com.cesar.sharing.entity.Period;

/**
 * <p>
 *  This Enum represents all available status for {@link Period}
 *  for now, we have the following values:
 *  - OPENED
 *  - CLOSED
 */
public enum PeriodStatus {

    OPENED("opened"), CLOSED("closed");


    /** Enum */
    private String status;

    /**
     * <p>
     *  We make this private to for use the
     *  constants values to instantiate
     *  this enum.
     * @param status
     */
    private PeriodStatus(String status) {
        this.status = status;
    }

    /**
     * <p>
     *  Check if the string value informed is compatible with the values
     *  exposed by this enum {@link PeriodStatus}
     * @param value Must he one of {@link PeriodStatus}
     * @return true if is a valid value, otherwise, false.
     */
    public static boolean isValid(String value) {
        for (PeriodStatus status : values()) {
            if (status.name().equalsIgnoreCase(value))
                return true;
        }
        return false;
    }
}
