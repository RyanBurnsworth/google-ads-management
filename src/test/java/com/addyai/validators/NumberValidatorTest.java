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

package com.addyai.validators;

import com.addyai.utils.validators.NumberValidator;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = NumberValidator.class)
public class NumberValidatorTest {

    @Test
    void testIsNumericReturnsTrue() {
        assertTrue(NumberValidator.isNumeric("1000"));
    }

    @Test
    void testIsNumericReturnFalse() {
        assertFalse(NumberValidator.isNumeric("123hello123"));
    }

    @Test
    void testContainsDigitsReturnsTrue() {
        assertTrue(NumberValidator.containsDigits("this/is/the/digit/test/123"));
    }

    @Test
    void testContainsDigitsReturnsFalse() {
        assertFalse(NumberValidator.containsDigits("this/has/no/digits"));
    }
}
