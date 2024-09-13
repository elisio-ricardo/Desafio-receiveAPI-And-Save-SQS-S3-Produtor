package com.elisio.sensidia.DesafioSensidia.framework.exception;

public class ValidationParseJson extends RuntimeException {

    private String error;
    public ValidationParseJson(String message) {
        this.error = message;
    }

    public String getError() {
        return error;
    }
}
