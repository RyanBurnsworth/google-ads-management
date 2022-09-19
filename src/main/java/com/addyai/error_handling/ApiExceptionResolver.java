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

package com.addyai.error_handling;

import com.addyai.error_handling.exceptions.InvalidRequestException;
import com.addyai.error_handling.exceptions.ServiceFailureException;
import com.addyai.utils.helpers.StringHelper;
import com.google.ads.googleads.v11.errors.GoogleAdsException;

import static com.addyai.utils.misc.Constants.UNKNOWN_SERVICE_ERROR;

public class ApiExceptionResolver {
    /**
     * Resolve to the proper exception given the error type. If exception type is unknown pass it forward.
     *
     * @param exception the exception that has been thrown
     * @return the resolved exception
     */
    public static Exception doResolveException(Exception exception) {
        String errorCode = "";
        String errorMessage = "";

        // check if exception is specific to Google Ads
        if (exception instanceof GoogleAdsException) {
            GoogleAdsException ex = (GoogleAdsException) exception;

            // extract the error type and error message
            if (ex.getGoogleAdsFailure() != null && ex.getGoogleAdsFailure().getErrorsList().size() > 0) {
                String uncleanErrorType = ex.getGoogleAdsFailure().getErrorsList().get(0).getErrorCode().toString();
                errorCode = StringHelper.doCleanErrorCode(uncleanErrorType);
                errorMessage = ex.getGoogleAdsFailure().getErrorsList().get(0).getMessage();
            }

            return new InvalidRequestException(errorCode, errorMessage);
        }

        return new ServiceFailureException(UNKNOWN_SERVICE_ERROR, exception.getMessage());
    }
}
