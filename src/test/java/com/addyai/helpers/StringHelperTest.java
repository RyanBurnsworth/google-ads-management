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

import com.addyai.utils.helpers.StringHelper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = StringHelper.class)
public class StringHelperTest {
    @Test
    void testFindPositionOfCharInString() {
        String testString = "NestleQuik";
        int positionOfQ = StringHelper.findPositionOfCharInString(testString, 'Q');

        assertEquals(6, positionOfQ);
    }

    @Test
    void testCleanErrorCode() {
        String errorCodeString = "campaignError: Duplicate campaign name\n";
        String cleanString = StringHelper.doCleanErrorCode(errorCodeString);

        assertEquals("Duplicate campaign name", cleanString);
    }
}
