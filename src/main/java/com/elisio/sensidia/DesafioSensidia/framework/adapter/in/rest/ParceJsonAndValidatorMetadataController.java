package com.elisio.sensidia.DesafioSensidia.framework.adapter.in.rest;

import com.elisio.sensidia.DesafioSensidia.framework.adapter.in.dto.UploadResponseDTO;
import com.elisio.sensidia.DesafioSensidia.framework.exception.ValidationErrorException;
import com.elisio.sensidia.DesafioSensidia.framework.exception.ValidationParseJsonException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Slf4j
@Validated
@Service
public class ParceJsonAndValidatorMetadataController {

    private final ObjectMapper objectMapper;
    private final Validator validator;


    public ParceJsonAndValidatorMetadataController(ObjectMapper objectMapper, Validator validator) {
        this.objectMapper = objectMapper;
        this.validator = validator;
    }

    public UploadResponseDTO parceJsonToUploadResponseDto(String metadataUpload) {

        try {
            log.info("Iniciando a transformação do json e validação dos campos");
            var metadata = objectMapper.readValue(metadataUpload, UploadResponseDTO.class);

            Set<ConstraintViolation<UploadResponseDTO>> violations = validator.validate(metadata);

            if (!violations.isEmpty()) {
                log.error("Adicionando erros de validação: " + violations.toString());
                List<String> errorMessage = new ArrayList<>();
                for (ConstraintViolation<UploadResponseDTO> violation : violations) {
                    errorMessage.add(violation.getMessageTemplate());
                }
                throw new ValidationErrorException(errorMessage);
            }

            return metadata;

        } catch (JsonProcessingException e) {
            throw new ValidationParseJsonException("Error to parse Json: " + e.getOriginalMessage());
        }


    }
}
