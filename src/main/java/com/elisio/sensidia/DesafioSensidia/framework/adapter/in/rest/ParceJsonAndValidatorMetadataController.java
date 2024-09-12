package com.elisio.sensidia.DesafioSensidia.framework.adapter.in.rest;

import com.elisio.sensidia.DesafioSensidia.domain.entities.Upload;
import com.elisio.sensidia.DesafioSensidia.framework.exception.ValidationError;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Slf4j
@Validated
public class ParceJsonAndValidatorMetadataController {

    public static Upload parceJsonUpload(String metadataUpload, Validator validator) {
        ObjectMapper objectMapper = new ObjectMapper();

        Upload metadata;

        try {
            log.info("Iniciando a transformação do json e validação dos campos");
            metadata = objectMapper.readValue(metadataUpload, Upload.class);

            Set<ConstraintViolation<Upload>> violations = validator.validate(metadata);

            if (!violations.isEmpty()) {
                log.error("Adicionando erros: " + violations.toString());
                List<String> errorMessage = new ArrayList<>();
                for (ConstraintViolation<Upload> violation : violations) {
                    errorMessage.add(violation.getMessageTemplate());
                }
                throw new ValidationError(errorMessage);
            }

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return metadata;
    }
}
