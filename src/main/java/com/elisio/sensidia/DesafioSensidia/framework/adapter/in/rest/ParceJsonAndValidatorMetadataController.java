package com.elisio.sensidia.DesafioSensidia.framework.adapter.in.rest;

import com.elisio.sensidia.DesafioSensidia.framework.adapter.in.dto.UploadResponseDTO;
import com.elisio.sensidia.DesafioSensidia.framework.exception.ValidationError;
import com.elisio.sensidia.DesafioSensidia.framework.exception.ValidationParseJson;
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

    public static UploadResponseDTO parceJsonToUploadResponseDto(String metadataUpload, Validator validator) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            log.info("Iniciando a transformação do json e validação dos campos");
            var metadata = objectMapper.readValue(metadataUpload, UploadResponseDTO.class);

            Set<ConstraintViolation<UploadResponseDTO>> violations = validator.validate(metadata);

            if (!violations.isEmpty()) {
                log.error("Adicionando erros: " + violations.toString());
                List<String> errorMessage = new ArrayList<>();
                for (ConstraintViolation<UploadResponseDTO> violation : violations) {
                    errorMessage.add(violation.getMessageTemplate());
                }
                throw new ValidationError(errorMessage);
            }

            return metadata;

        } catch (JsonProcessingException e) {
            throw new ValidationParseJson("Error to parse Json: " + e.getOriginalMessage());
        }


    }
}
