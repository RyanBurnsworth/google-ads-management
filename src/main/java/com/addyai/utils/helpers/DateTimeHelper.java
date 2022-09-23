/*
 * Copyright (c) 2022.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version. This program
 * is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 *
 */

package com.addyai.utils.helpers;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public class DateTimeHelper {
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

    public static Timestamp getCurrentTimestamp() {
        return new Timestamp(date.getTime());
    }
}
