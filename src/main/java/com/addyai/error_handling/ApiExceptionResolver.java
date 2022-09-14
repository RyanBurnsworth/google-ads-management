package com.addyai.error_handling;

import com.addyai.error_handling.exceptions.InvalidRequestException;
import com.addyai.error_handling.exceptions.ServiceFailureException;
import com.google.ads.googleads.v11.errors.GoogleAdsException;

import static com.addyai.utils.Constants.INTERNAL_ERROR;
import static com.addyai.utils.Constants.UNKNOWN_SERVICE_ERROR;

public class ApiExceptionResolver {
    /**
     * Resolve to the proper exception given the error type. If exception type is unknown pass it forward.
     *
     * @param exception the exception that has been thrown
     * @return the resolved exception
     */
    public static Exception doResolveException(Exception exception) {
        String errorType = "";
        String errorCode = "";
        String errorMessage = "";

        // check if exception is specific to Google Ads
        if (exception instanceof GoogleAdsException) {
            GoogleAdsException ex = (GoogleAdsException) exception;

            // extract the error code as a String
            if (ex.getStatusCode() != null && ex.getStatusCode().getTransportCode() != null)
                errorCode = ex.getStatusCode().getTransportCode().toString();

            // extract the error type and error message
            if (ex.getGoogleAdsFailure() != null && ex.getGoogleAdsFailure().getErrorsList().size() > 0) {
                errorType = ex.getGoogleAdsFailure().getErrorsList().get(0).getErrorCode().getRequestError().toString();
                errorMessage = ex.getGoogleAdsFailure().getErrorsList().get(0).getMessage();
            }

            return new InvalidRequestException(errorType, errorCode, errorMessage);
        }

        return new ServiceFailureException(INTERNAL_ERROR, UNKNOWN_SERVICE_ERROR, exception.getMessage());
    }
}
