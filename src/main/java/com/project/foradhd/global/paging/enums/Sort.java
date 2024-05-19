package com.project.foradhd.global.paging.enums;

import java.util.Objects;
import java.util.Optional;

public enum Sort {

    ;

    public enum Direction {

        ASC, DESC;

        public static Optional<Direction> from(String value) {
            value = value.toUpperCase();
            for (Direction direction : Direction.values()) {
                if (Objects.equals(direction.name(), value)) {
                    return Optional.of(direction);
                }
            }
            return Optional.empty();
        }
    }
}
