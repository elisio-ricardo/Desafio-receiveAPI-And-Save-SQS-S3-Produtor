package com.elisio.sensidia.DesafioSensidia.framework.adapter.in.rest;


import com.elisio.sensidia.DesafioSensidia.application.port.in.UploadPortIn;
import com.elisio.sensidia.DesafioSensidia.domain.entities.Upload;
import com.elisio.sensidia.DesafioSensidia.framework.adapter.in.dto.UploadResponseDTO;
import com.elisio.sensidia.DesafioSensidia.framework.exception.ValidationError;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/uploads")
@Tag(name = "Uploads REST")
@Validated
public class UploadController {


    private final Validator validator;

    private final UploadPortIn uploadPortIn;

    public UploadController(Validator validator, UploadPortIn uploadPortIn) {
        this.validator = validator;
        this.uploadPortIn = uploadPortIn;
    }


    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<UploadResponseDTO> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestPart("metadataUpload") String metadataUpload
    ) {
        log.info("Iniciando Chamada com os dados: " + metadataUpload);
        try {
            Upload metadata = ParceJsonAndValidatorMetadataController.parceJsonUpload(metadataUpload, validator);
            log.info(metadata.toString());
            UploadResponseDTO savedUpload = uploadPortIn.uploadSQSAndS3(file, metadata);

            return ResponseEntity.ok(savedUpload);
        } catch (ValidationError e) {
            log.error("Erro: " + e.getErrors());
            throw new ValidationError(e.getErrors());
        }
    }
}
