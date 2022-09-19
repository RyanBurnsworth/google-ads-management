package com.addyai.error_handling;

import com.addyai.error_handling.exceptions.InvalidRequestException;
import com.addyai.error_handling.exceptions.ServiceFailureException;
import com.addyai.utils.helpers.StringHelper;
import com.google.ads.googleads.v11.errors.GoogleAdsException;

import static com.addyai.utils.misc.Constants.INTERNAL_ERROR;
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
