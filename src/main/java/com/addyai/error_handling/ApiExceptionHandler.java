/*
 * Copyright (c) 2022.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version. This program
 * is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 *
 */

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
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(ex.getErrorCode(), ex.getErrorMessage());
        return new ResponseEntity<>(apiErrorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ServiceFailureException.class)
    public final ResponseEntity<ApiErrorResponse> handleServiceFailureException(ServiceFailureException ex, WebRequest request) {
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(ex.getErrorCode(), ex.getErrorMessage());
        return new ResponseEntity<>(apiErrorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NotFoundException.class)
    public final ResponseEntity<ApiErrorResponse> handleNotFoundException(NotFoundException ex, WebRequest request) {
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(ex.getErrorCode(), ex.getErrorMessage());
        return new ResponseEntity<>(apiErrorResponse, HttpStatus.NOT_FOUND);
    }
}
