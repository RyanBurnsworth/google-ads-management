package com.addyai.utils.helpers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateHelper {
    private static final Date date = new Date();

    public static String getCurrentEpochTimeAsString() {
        return Long.toString(date.getTime());
    }

    public static String getCurrentDate() {
        LocalDateTime ldt = LocalDateTime.now();
        DateTimeFormatter format1 = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);

        return format1.format(ldt);
    }

    public static String getCurrentDatePlusYears(int years) {
        LocalDateTime ldt = LocalDateTime.now().plusYears(years);
        DateTimeFormatter format1 = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);

        return format1.format(ldt);
    }
}
