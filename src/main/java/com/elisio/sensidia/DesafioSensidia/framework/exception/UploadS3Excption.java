package com.elisio.sensidia.DesafioSensidia.framework.exception;

public class UploadS3Excption extends RuntimeException {
    private String error;

    public UploadS3Excption(String message) {
        this.error = message;
    }

    public String getError() {
        return error;
    }

}

