package com.nhat.demoSpringbooRestApi.exceptions;

import java.util.List;

public class ErrorResponse {
    private String message;
    private Object details;

    public ErrorResponse(String message, Object details) {
        this.message = message;
        this.details = details;
    }

    public ErrorResponse() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getDetails() {
        return details;
    }

    public void setDetails(Object details) {
        this.details = details;
    }
}
