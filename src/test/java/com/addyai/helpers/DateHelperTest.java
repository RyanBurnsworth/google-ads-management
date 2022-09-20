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

package com.addyai.helpers;

import com.addyai.utils.helpers.DateHelper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = DateHelper.class)
public class DateHelperTest {
    @Test
    void testGetEpochTimeAsStringIsValid() {
        assertFalse(DateHelper.getCurrentEpochTimeAsString().isEmpty());
    }

    @Test
    void getCurrentDatePlus10Years() {
        LocalDateTime ldt = LocalDateTime.now();
        LocalDateTime ldt2 = LocalDateTime.now().plusYears(10);

        DateTimeFormatter format1 = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);

        String currentDate = format1.format(ldt);
        String futureDate = format1.format(ldt2);

        String year = currentDate.substring(0,4);
        int yearPlus10 = Integer.parseInt(year) + 10;
        String newYear = String.valueOf(yearPlus10);

        String newDate = currentDate.replace(year, newYear);

        assertEquals(newDate, futureDate);
    }
}
