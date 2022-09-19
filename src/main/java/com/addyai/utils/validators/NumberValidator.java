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
