package com.project.foradhd.global.util;

public abstract class StringUtil {

    public static String strip(String value) {
        if (value == null) return null;
        return value.strip();
    }

    public static String addPrefix(String value, String prefix) {
        if (value == null) return null;
        if (value.startsWith(prefix)) return value;
        return prefix + value;
    }
}
