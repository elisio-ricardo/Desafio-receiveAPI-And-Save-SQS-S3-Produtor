package com.elisio.sensidia.DesafioSensidia.framework.exception;

public class ValidateNullPointerException extends RuntimeException {

    private String error;

    public ValidateNullPointerException(String message) {
        this.error = message;
    }

    public String getError() {
        return error;
    }
}