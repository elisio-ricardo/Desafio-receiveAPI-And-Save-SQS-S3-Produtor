package com.elisio.sensidia.DesafioSensidia.framework.adapter.in.rest;


import com.elisio.sensidia.DesafioSensidia.application.port.in.UploadPortIn;
import com.elisio.sensidia.DesafioSensidia.domain.entities.Upload;
import com.elisio.sensidia.DesafioSensidia.framework.adapter.in.dto.UploadResponseDTO;
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
            ObjectMapper objectMapper = new ObjectMapper();
            Upload metadata = objectMapper.readValue(metadataUpload, Upload.class);
            Set<ConstraintViolation<Upload>> violations = validator.validate(metadata);
            log.info(violations.toString());
            if (!violations.isEmpty()) {
                StringBuilder errorMessage = new StringBuilder("Erro de validação: ");
                for (ConstraintViolation<Upload> violation : violations) {
                    errorMessage.append(violation.getMessage()).append("; ");
                }
               // return ResponseEntity.badRequest().body(errorMessage.toString());
                return ResponseEntity.badRequest().build();
            }
            log.info(metadata.toString());

            UploadResponseDTO savedUpload = uploadPortIn.saveUpload(file, metadata);

            return ResponseEntity.ok(savedUpload);
        } catch (Exception e) {
            log.info("Erro: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        }

    }

}
