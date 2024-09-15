package com.elisio.sensidia.DesafioSensidia.framework.exception;

public class AwsException extends RuntimeException {
    private String error;

    public AwsException(String message) {
        this.error = message;
    }

    public String getError() {
        return error;
    }

}