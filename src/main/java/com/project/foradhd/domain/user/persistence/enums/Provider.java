package com.project.foradhd.domain.user.persistence.enums;

public enum Provider {

    NAVER, KAKAO, FACEBOOK, APPLE;

    public static Provider from(String value) {
        return Provider.valueOf(value.toUpperCase());
    }
}
