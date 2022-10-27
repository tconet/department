package com.cesar.sharing.types;

/**
 * <p>
 *  This Enum represents all available status for {@link com.cesar.sharing.entity.ResourceSharing}
 *  for now, we have the following values:
 *  - OPEN
 *  - CLOSED
 */
public enum ResourceSharingStatus {

    OPENED("OPENED"), APPROVED("APPROVED"), REJECTED("REJECTED");

    /** Enum */
    private String status;

    /**
     * <p>
     *  We make this private to for use the
     *  constants values to instantiate
     *  this enum.
     * @param status
     */
    private ResourceSharingStatus(String status) {
        this.status = status;
    }

    /**
     * <p>
     *  Check if the string value informed is compatible with the values
     *  exposed by this enum {@link ResourceSharingStatus}
     * @param value Must he one of {@link ResourceSharingStatus}
     * @return true if is a valid value, otherwise, fale.
     */
    public static boolean isValid(String value) {
        for (ResourceSharingStatus status : values()) {
            if (status.name().equalsIgnoreCase(value))
                return true;
        }
        return false;
    }

}
