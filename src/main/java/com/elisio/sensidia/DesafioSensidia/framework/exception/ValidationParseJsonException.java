package com.elisio.sensidia.DesafioSensidia.framework.exception;

public class ValidationParseJsonException extends RuntimeException {

    private String error;
    public ValidationParseJsonException(String message) {
        this.error = message;
    }

    public String getError() {
        return error;
    }
}
