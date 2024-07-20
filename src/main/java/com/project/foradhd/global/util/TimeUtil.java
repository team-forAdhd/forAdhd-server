package com.project.foradhd.global.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

public abstract class TimeUtil {

    private static final ZoneOffset DEFAULT_ZONE_OFFSET = ZoneOffset.of("+09:00");
    private static final ZoneId DEFAULT_ZONE_ID = ZoneId.of("Asia/Seoul");

    public static long toEpochSecond(LocalDateTime localDateTime) {
        return localDateTime.toEpochSecond(DEFAULT_ZONE_OFFSET);
    }

    public static LocalDateTime toLocalDateTime(long epochSecond) {
        Instant instant = Instant.ofEpochSecond(epochSecond);
        return instant.atOffset(DEFAULT_ZONE_OFFSET).toLocalDateTime();
    }
}
