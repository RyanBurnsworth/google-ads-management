package com.addyai.error_handling.exceptions;

public class NotFoundException extends BaseApiException {

    public NotFoundException(String errorType, String errorCode, String errorMessage) {
        super(errorType, errorCode, errorMessage);
    }
}
