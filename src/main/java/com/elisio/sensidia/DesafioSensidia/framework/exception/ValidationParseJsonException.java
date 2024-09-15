package com.elisio.sensidia.DesafioSensidia.framework.exception;


import lombok.Data;

@Data
public class ValidationParseJsonException extends RuntimeException {

    private String error;
    public ValidationParseJsonException(String message) {
        this.error = message;
    }
}
