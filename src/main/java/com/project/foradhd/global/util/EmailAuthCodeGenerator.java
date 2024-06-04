package com.project.foradhd.global.util;

import java.util.Random;

public class EmailAuthCodeGenerator {

    private static final int AUTH_CODE_LENGTH = 6;
    private static final int MIN_RANDOM_NUMBER_INCLUSIVE = 0;
    private static final int MAX_RANDOM_NUMBER_EXCLUSIVE = 10;
    private static final int ALLOWED_MAX_DUPLICATED_TIMES = 2;

    public static String generate() {
        StringBuilder builder = new StringBuilder();
        Random random = new Random(System.currentTimeMillis());
        int[] numberCheckTable = new int[10];
        do {
            int number = generateRandomNumber(random, numberCheckTable);
            builder.append(number);
        } while (builder.length() < AUTH_CODE_LENGTH);
        return builder.toString();
    }

    private static int generateRandomNumber(Random random, int[] numberCheckTable) {
        int number = random.nextInt(MIN_RANDOM_NUMBER_INCLUSIVE, MAX_RANDOM_NUMBER_EXCLUSIVE);
        while (!checkDuplicatedNumberTimes(number, numberCheckTable)) {
            number = (number + 1) % MAX_RANDOM_NUMBER_EXCLUSIVE;
        }
        numberCheckTable[number]++;
        return number;
    }

    private static boolean checkDuplicatedNumberTimes(int number, int[] numberCheckTable) {
        return numberCheckTable[number] < ALLOWED_MAX_DUPLICATED_TIMES;
    }
}
