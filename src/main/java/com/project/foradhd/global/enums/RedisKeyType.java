package com.project.foradhd.global.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum RedisKeyType {

    USER_REFRESH_TOKEN("user", "refresh-token"),
    USER_EMAIL_AUTH_CODE("user", "email-auth-code"),
    HOSPITAL_OPERATION_DETAILS("hospital", "operation-details");

    private final String domain;
    private final String dataType;
    private static final String KEY_SEPARATOR = ":";

    public String getKey(String id) {
        return new StringBuilder()
                .append(domain).append(KEY_SEPARATOR)
                .append(dataType).append(KEY_SEPARATOR)
                .append(id)
                .toString();
    }
}
