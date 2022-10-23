package com.cesar.sharing.types;

import com.cesar.sharing.entity.Resource;

/**
 * <p>
 *  This Enum represents all available status for {@link Resource}
 *  for now, we have the following values:
 *  - HUMAN
 *  - COMPANY
 *  - PHYSICAL
 */
public enum ResourceStatus {

    HUMAN("human"), COMPANY("company"), PHYSICAL("physical");

    /** Enum */
    private String status;

    /**
     * <p>
     *  We make this private to for use the
     *  constants values to instantiate
     *  this enum.
     * @param status
     */
    private ResourceStatus(String status) {
        this.status = status;
    }

    /**
     * <p>
     *  Check if the string value informed is compatible with the values
     *  exposed by this enum {@link ResourceStatus}
     * @param value Must he one of {@link ResourceStatus}
     * @return true if is a valid value, otherwise, false.
     */
    public static boolean isValid(String value) {
        for (ResourceStatus status : values()) {
            if (status.name().equalsIgnoreCase(value))
                return true;
        }
        return false;
    }
}
