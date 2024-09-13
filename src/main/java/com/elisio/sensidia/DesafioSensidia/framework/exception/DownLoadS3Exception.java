package com.elisio.sensidia.DesafioSensidia.framework.exception;

public class DownLoadS3Exception extends RuntimeException {
    private String error;

    public DownLoadS3Exception(String message) {
        this.error = message;
    }

    public String getError() {
        return error;
    }
}
