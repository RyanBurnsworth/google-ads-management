package com.addyai.error_handling.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_GATEWAY)
public class ServiceFailureException extends BaseApiException {

    public ServiceFailureException(String errorType, String errorCode, String errorMessage) {
        super(errorType, errorCode, errorMessage);
    }
}
