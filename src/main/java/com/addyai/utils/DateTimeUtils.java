package com.addyai.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTimeUtils {
    public static String getCurrentDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        return dateFormat.format(date).replace("/", "");
    }

    public static String getFutureDate(int numDaysInFuture) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, numDaysInFuture);
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        return dateFormat.format(cal.getTime()).replace("/", "");
    }
}
