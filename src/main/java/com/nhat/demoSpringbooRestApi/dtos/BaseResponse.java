package com.nhat.demoSpringbooRestApi.dtos;

public class BaseResponse {

    private boolean isSuccess;
    private String message;
    private Object result;
    private Object errors;

    public BaseResponse() {
    }

    public BaseResponse(boolean isSuccess, String message, Object result, Object errors) {
        this.isSuccess = isSuccess;
        this.message = message;
        this.errors = errors;
        this.result = result;

    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public Object getErrors() {
        return errors;
    }

    public void setErrors(Object errors) {
        this.errors = errors;
    }
}
