package com.project.foradhd.global.util;

public abstract class AverageCalculator {

    public static Double calculate(Long total, Long count) {
        if (total == null || count == null || count == 0) return 0D;
        return (double) total / count;
    }
}
