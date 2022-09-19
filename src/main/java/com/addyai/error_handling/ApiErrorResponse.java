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

import java.sql.Timestamp;
import java.util.Date;

public class ApiErrorResponse {
    private final Date timeStamp;
    private final String errorCode;
    private final String errorMessage;

    public ApiErrorResponse(String errorCode, String errorMessage) {
        super();
        Date date = new Date();

        this.timeStamp = new Timestamp(date.getTime());
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
