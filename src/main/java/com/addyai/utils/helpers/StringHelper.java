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
