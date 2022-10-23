package com.cesar.sharing.types;

import com.cesar.sharing.entity.CostCenter;

/**
 * <p>
 *  This Enum represents all available status for {@link CostCenter}
 *  for now, we have the following values:
 *  - OPEN
 *  - CLOSED
 */
public enum CostCenterStatus {

    OPEN("open"), CLOSED("closed");

    /** Enum */
    private String status;

    /**
     * <p>
     *  We make this private to for use the
     *  constants values to instantiate
     *  this enum.
     * @param status
     */
    private CostCenterStatus(String status) {
        this.status = status;
    }

    /**
     * <p>
     *  Check if the string value informed is compatible with the values
     *  exposed by this enum {@link CostCenterStatus}
     * @param value Must he one of {@link CostCenterStatus}
     * @return true if is a valid value, otherwise, fale.
     */
    public static boolean isValid(String value) {
        for (CostCenterStatus status : values()) {
            if (status.name().equalsIgnoreCase(value))
                return true;
        }
        return false;
    }
}
