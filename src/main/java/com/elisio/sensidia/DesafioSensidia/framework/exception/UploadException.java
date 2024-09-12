package com.elisio.sensidia.DesafioSensidia.framework.exception;


import lombok.Data;

@Data
public class UploadException {
    private String message;

    public UploadException(String message) {
        this.message = message;
    }
}