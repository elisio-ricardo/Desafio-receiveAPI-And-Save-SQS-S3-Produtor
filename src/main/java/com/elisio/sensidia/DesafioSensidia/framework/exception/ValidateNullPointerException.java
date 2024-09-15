package com.elisio.sensidia.DesafioSensidia.framework.exception;


import lombok.Data;

@Data
public class ValidateNullPointerException extends RuntimeException {

    private String error;

    public ValidateNullPointerException(String message) {
        this.error = message;
    }


}