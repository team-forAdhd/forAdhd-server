package com.project.foradhd.domain.user.persistence.enums;

import java.util.Optional;

public enum Provider {

    NAVER, KAKAO, GOOGLE, APPLE;

    public static Optional<Provider> from(String value) {
        try {
            Provider provider = Provider.valueOf(value.toUpperCase());
            return Optional.of(provider);
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }
}
