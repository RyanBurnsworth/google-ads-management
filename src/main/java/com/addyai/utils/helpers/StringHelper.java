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

public class StringHelper {
    /**
     * Get the position of the first occurrence of character in a string
     *
     * @param haystack the string to be searched
     * @param needle   the character to be found
     * @return the position of the character if found, else -1
     */
    public static int findPositionOfCharInString(String haystack, char needle) {
        for (int i = 0; i < haystack.length(); i++)
            if (haystack.charAt(i) == needle)
                return i;
        return -1;
    }

    public static String doCleanErrorCode(String uncleanErrorCode) {
        int positionOfColonInString = findPositionOfCharInString(uncleanErrorCode, ':');

        // if the colon is not found in the errorCode return an empty string
        if (positionOfColonInString == -1) return "";

        // accounts for the colon and proceeding space in string
        int startingPosition = positionOfColonInString + 2;

        // update error code to remove \n
        if (uncleanErrorCode.contains("\n"))
            uncleanErrorCode = uncleanErrorCode.replace("\n", "");

        return uncleanErrorCode.substring(startingPosition);
    }
}
