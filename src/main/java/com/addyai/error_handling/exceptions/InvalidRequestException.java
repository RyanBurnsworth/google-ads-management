package com.addyai.error_handling.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidRequestException extends BaseApiException {

    public InvalidRequestException(String errorType, String errorCode, String errorMessage) {
        super(errorType, errorCode, errorMessage);
    }
}
