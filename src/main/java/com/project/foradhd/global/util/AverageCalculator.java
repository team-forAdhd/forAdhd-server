package com.project.foradhd.global.util;

public abstract class AverageCalculator {

    public static Double calculate(Long totalSum, Long totalCount) {
        if (totalSum == null || totalCount == null || totalCount == 0) return 0D;
        return (double) totalSum / totalCount;
    }
}
