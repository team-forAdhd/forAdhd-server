package com.project.foradhd.domain.hospital.web.enums;

public enum HospitalOperationStatus {

    OPEN, CLOSED, UNKNOWN;

    public static HospitalOperationStatus from(boolean openNow) {
        if (openNow) return OPEN;
        return CLOSED;
    }
}
