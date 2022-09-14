package com.addyai.error_handling.exceptions;

public abstract class BaseApiException extends RuntimeException {
    private final static long serialVersionUID = 1L;
    private final String errorCode;
    private final String errorType;
    private final String errorMessage;

    public BaseApiException(String errorType, String errorCode, String errorMessage) {
        super("Error Code: " + errorCode + " Error Type: " + errorType + " Error Message: " + errorMessage);

        this.errorCode = errorCode;
        this.errorType = errorType;
        this.errorMessage = errorMessage;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorType() {
        return errorType;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
