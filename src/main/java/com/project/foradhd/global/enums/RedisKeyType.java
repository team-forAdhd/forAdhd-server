package com.project.foradhd.global.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum RedisKeyType {

    USER_REFRESH_TOKEN("user", "refresh-token"),
    USER_EMAIL_AUTH_CODE("user", "email-auth-code");

    private final String domain;
    private final String dataType;
    private static final String KEY_SEPERATOR = ":";

    public String getKey(String id) {
        StringBuilder builder = new StringBuilder();
        builder.append(domain).append(KEY_SEPERATOR)
                .append(dataType).append(KEY_SEPERATOR)
                .append(id);
        return builder.toString();
    }
}
