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

    @ExceptionHandler(ValidationErrorException.class)
    public ResponseEntity<ErrosDetail> handleUploadException(ValidationErrorException ex) {
        log.info("Gerando o erro: ValidationError");
        List<UploadException> errors = ex.getErrors().stream().
                map(erro -> new UploadException(erro))
                .collect(Collectors.toList());
        ErrosDetail errosDetail = new ErrosDetail(new Date(), HttpStatus.BAD_REQUEST.value(), errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errosDetail);
    }

    @ExceptionHandler(ValidationParseJsonException.class)
    public ResponseEntity<ErrosDetail> handleJsonException(ValidationParseJsonException ex) {
        log.info("Gerando o erro: JsonProcessingException");
        List<UploadException> errors = new ArrayList<>();
        errors.add(new UploadException(ex.getError()));
        ErrosDetail errosDetail = new ErrosDetail(new Date(), HttpStatus.BAD_REQUEST.value(), errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errosDetail);
    }


    @ExceptionHandler(AwsException.class)
    public ResponseEntity<ErrosDetail> handleAwsExceptionException(AwsException ex) {
        log.info("Gerando o erro: UploadDynamoDbException");
        List<UploadException> errors = new ArrayList<>();
        errors.add(new UploadException(ex.getError()));
        ErrosDetail errosDetail = new ErrosDetail(new Date(), HttpStatus.INTERNAL_SERVER_ERROR.value(), errors);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errosDetail);
    }


}
