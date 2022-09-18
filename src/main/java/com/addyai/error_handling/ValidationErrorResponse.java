package com.addyai.error_handling;

public class ValidationErrorResponse {
    private final String errorType;
    private final String errorCode;
    private final String errorMessage;

    public ValidationErrorResponse(String errorType, String errorCode, String errorMessage) {
        super();

        this.errorCode = errorCode;
        this.errorType = errorType;
        this.errorMessage = errorMessage;
    }

    public String getErrorType() {
        return errorType;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
