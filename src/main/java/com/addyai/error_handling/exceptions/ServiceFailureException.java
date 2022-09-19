package com.addyai.error_handling.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class ServiceFailureException extends BaseApiException {

    public ServiceFailureException(String errorCode, String errorMessage) {
        super(errorCode, errorMessage);
    }
}
