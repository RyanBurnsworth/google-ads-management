package com.addyai.error_handling;

public class ValidationErrorResponse {
    private final String errorCode;
    private final String errorMessage;

    public ValidationErrorResponse(String errorCode, String errorMessage) {
        super();

        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
