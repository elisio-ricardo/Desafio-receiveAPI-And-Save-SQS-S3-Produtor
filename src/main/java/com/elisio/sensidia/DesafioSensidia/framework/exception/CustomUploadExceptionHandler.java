package com.elisio.sensidia.DesafioSensidia.framework.exception;


import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class CustomUploadExceptionHandler {

    @ExceptionHandler(ValidationError.class)
    public ResponseEntity<ErrosDetail> handleUploadException(ValidationError ex) {
        log.error("Cheguei aqui: " + ex.getErrors().toString());
        List<UploadException> errors = ex.getErrors().stream().
                map(erro -> new UploadException(erro))
                .collect(Collectors.toList());
        ErrosDetail errosDetail = new ErrosDetail(new Date(), HttpStatus.BAD_REQUEST.value(), errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errosDetail);
    }

    @ExceptionHandler(JsonProcessingException.class)
    public ResponseEntity<ErrosDetail> handleJsonException(JsonProcessingException ex) {
        log.error("Cheguei aqui: " + ex.getMessage());
        List<UploadException> errors = new ArrayList<>();
        errors.add(new UploadException(ex.getMessage()));
        ErrosDetail errosDetail = new ErrosDetail(new Date(), HttpStatus.BAD_REQUEST.value(), errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errosDetail);
    }


}
