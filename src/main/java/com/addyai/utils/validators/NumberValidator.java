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

package com.addyai.utils.validators;

public class NumberValidator {
    public static boolean isNumeric(String numberValue) {
        try {
            Double.parseDouble(numberValue);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    public static boolean containsDigits(String input) {
        char[] chars = input.toCharArray();

        for (char c : chars) {
            if (Character.isDigit(c)) {
                return true;
            }
        }
        return false;
    }
}
