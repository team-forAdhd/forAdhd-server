package com.project.foradhd.global.client.dto.response;

import lombok.Getter;

import java.util.List;

@Getter
public class GooglePlaceOpeningHoursResponse {

    private CurrentOpeningHours currentOpeningHours;

    @Getter
    public static class CurrentOpeningHours {

        private boolean openNow;
        private List<Period> periods;
    }

    @Getter
    public static class Period {

        private Time open = new Time();
        private Time close = new Time();
    }

    @Getter
    public static class Time {

        private int day;
        private int hour;
        private int minute;
        private Date date = new Date();
    }

    @Getter
    public static class Date {
        private int year;
        private int month;
        private int day;
    }
}
