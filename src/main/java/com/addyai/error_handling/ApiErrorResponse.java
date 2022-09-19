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
