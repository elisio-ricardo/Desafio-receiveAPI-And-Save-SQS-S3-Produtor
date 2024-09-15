package com.elisio.sensidia.DesafioSensidia.framework.adapter.in.rest;


import com.elisio.sensidia.DesafioSensidia.application.port.in.UploadPortIn;
import com.elisio.sensidia.DesafioSensidia.framework.adapter.in.dto.UploadResponseDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
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

    private final UploadPortIn uploadPortIn;

    private final ParceJsonAndValidatorMetadataController validatorMetadata;

    public UploadController(UploadPortIn uploadPortIn, ParceJsonAndValidatorMetadataController validatorMetadata) {
        this.uploadPortIn = uploadPortIn;
        this.validatorMetadata = validatorMetadata;
    }

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<UploadResponseDTO> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestPart("metadataUpload") String metadataUpload
    ) {
        log.info("Iniciando Chamada com os dados: " + metadataUpload);

        var metadata = validatorMetadata.parceJsonToUploadResponseDto(metadataUpload);
        log.info(metadata.toString());
        uploadPortIn.uploadService(file, metadata);
        log.info("Chamada para inserção da request de processing de dados concluida");
        return ResponseEntity.ok().body(metadata);

    }
}

