package com.addyai.error_handling;

import com.addyai.error_handling.exceptions.InvalidRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(InvalidRequestException.class)
    public final ResponseEntity<ApiErrorResponse> handleInvalidRequestException(InvalidRequestException ex, WebRequest request) {
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(ex.getErrorType(), ex.getErrorCode(), ex.getErrorMessage());
        return new ResponseEntity<>(apiErrorResponse, HttpStatus.BAD_REQUEST);
    }
}
