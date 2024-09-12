package com.elisio.sensidia.DesafioSensidia.framework.exception;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ErrosDetail {

    private Date timestamp;

    private int status;
    private List<UploadException> errors;

    public ErrosDetail(Date timestamp, int status, List<UploadException> errors) {
        this.timestamp = timestamp;
        this.status = status;
        this.errors = errors;
    }
}
