package com.department.types;

/**
 * <p>
 *  This Enum represents all available status for {@link com.department.entity.CostCenter}
 *  for now, we have the following values:
 *  - OPEN
 *  - CLOSED
 */
public enum CostCenterStatus {

    OPEN("Open"), CLOSED("Closed");

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
}
