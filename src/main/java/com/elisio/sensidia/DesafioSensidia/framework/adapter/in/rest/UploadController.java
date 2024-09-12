package com.elisio.sensidia.DesafioSensidia.framework.adapter.in.rest;


import com.elisio.sensidia.DesafioSensidia.application.port.in.UploadPortIn;
import com.elisio.sensidia.DesafioSensidia.domain.entities.Upload;
import com.elisio.sensidia.DesafioSensidia.framework.adapter.in.dto.UploadResponseDTO;
import com.elisio.sensidia.DesafioSensidia.framework.exception.ValidationError;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/uploads")
@Tag(name = "Uploads REST")
@Validated
public class UploadController {

    @Autowired
    private Validator validator;

    private final UploadPortIn uploadPortIn;

    public UploadController(UploadPortIn uploadPortIn) {
        this.uploadPortIn = uploadPortIn;
    }


    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<UploadResponseDTO> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestPart("metadataUpload") String metadataUpload
    ) {
        log.info("Iniciando Chamada com os dados: " + metadataUpload);
        try {
            Upload metadata = getUpload(metadataUpload);
            log.info(metadata.toString());
            UploadResponseDTO savedUpload = uploadPortIn.saveUpload(file, metadata);

            return ResponseEntity.ok(savedUpload);
        } catch (ValidationError e) {
            log.error("Erro: " + e.getErrors());
            throw new ValidationError(e.getErrors());
        }

    }


    private Upload getUpload(String metadataUpload) {
        ObjectMapper objectMapper = new ObjectMapper();
        Upload metadata = new Upload();
        try {
            metadata = objectMapper.readValue(metadataUpload, Upload.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        Set<ConstraintViolation<Upload>> violations = validator.validate(metadata);

        if (!violations.isEmpty()) {
            log.error("Adicionando erros: " + violations.toString());
            List<String> errorMessage = new ArrayList<>();
            for (ConstraintViolation<Upload> violation : violations) {
                errorMessage.add(violation.getMessageTemplate());
            }
            throw new ValidationError(errorMessage);
        }
        return metadata;
    }

}
