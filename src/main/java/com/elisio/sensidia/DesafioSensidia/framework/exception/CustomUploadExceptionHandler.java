package com.elisio.sensidia.DesafioSensidia.framework.exception;


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
        log.info("Gerando o erro: ValidationError");
        List<UploadException> errors = ex.getErrors().stream().
                map(erro -> new UploadException(erro))
                .collect(Collectors.toList());
        ErrosDetail errosDetail = new ErrosDetail(new Date(), HttpStatus.BAD_REQUEST.value(), errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errosDetail);
    }

    @ExceptionHandler(ValidationParseJson.class)
    public ResponseEntity<ErrosDetail> handleJsonException(ValidationParseJson ex) {
        log.info("Gerando o erro: JsonProcessingException");
        List<UploadException> errors = new ArrayList<>();
        errors.add(new UploadException(ex.getError()));
        ErrosDetail errosDetail = new ErrosDetail(new Date(), HttpStatus.BAD_REQUEST.value(), errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errosDetail);
    }

    @ExceptionHandler(DownLoadS3Exception.class)
    public ResponseEntity<ErrosDetail> downLoadS3Exception(DownLoadS3Exception ex) {
        log.info("Gerando o erro: DownLoadS3Exception");
        List<UploadException> errors = new ArrayList<>();
        errors.add(new UploadException(ex.getError()));
        ErrosDetail errosDetail = new ErrosDetail(new Date(), HttpStatus.BAD_REQUEST.value(), errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errosDetail);
    }


}
