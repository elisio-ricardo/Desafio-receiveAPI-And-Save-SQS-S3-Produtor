package com.elisio.sensidia.DesafioSensidia.framework.exception;


import lombok.Data;

import java.util.List;

@Data
public class ValidationError extends RuntimeException {

    private List<String> errors;




    public ValidationError(List<String> errors) {
        this.errors = errors;
    }

    public List<String> getErrors() {
        return errors;
    }

}
