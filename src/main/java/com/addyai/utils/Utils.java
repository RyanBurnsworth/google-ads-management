package com.addyai.utils;

public class Utils {
    /**
     * Converts micros bids to dollar values
     *
     * @param value the dollar value to be returned
     * @return an integer that converts to dollars as micros
     */
    public static int convertDollarsToMicros(String value) {
        return (int) (Float.parseFloat(value) * 1000000);
    }
}
