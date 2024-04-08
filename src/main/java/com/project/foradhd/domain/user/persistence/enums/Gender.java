package com.project.foradhd.domain.user.persistence.enums;

public enum Gender {

    MALE, FEMALE, UNKNOWN;

    public static Gender from(String value) {
        try {
            return Gender.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return UNKNOWN;
        }
    }
}
