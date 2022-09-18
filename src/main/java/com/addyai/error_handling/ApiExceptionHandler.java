package com.addyai.error_handling;

import com.addyai.error_handling.exceptions.InvalidRequestException;
import com.addyai.error_handling.exceptions.NotFoundException;
import com.addyai.error_handling.exceptions.ServiceFailureException;
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

    @ExceptionHandler(ServiceFailureException.class)
    public final ResponseEntity<ApiErrorResponse> handleServiceFailureException(ServiceFailureException ex, WebRequest request) {
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(ex.getErrorType(), ex.getErrorCode(), ex.getErrorMessage());
        return new ResponseEntity<>(apiErrorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NotFoundException.class)
    public final ResponseEntity<ApiErrorResponse> handleNotFoundException(NotFoundException ex, WebRequest request) {
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(ex.getErrorType(), ex.getErrorCode(), ex.getErrorMessage());
        return new ResponseEntity<>(apiErrorResponse, HttpStatus.NOT_FOUND);
    }
}
