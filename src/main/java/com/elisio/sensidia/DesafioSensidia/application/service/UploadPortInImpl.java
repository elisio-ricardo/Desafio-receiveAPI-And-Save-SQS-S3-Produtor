package com.elisio.sensidia.DesafioSensidia.application.service;

import com.elisio.sensidia.DesafioSensidia.application.port.in.UploadPortIn;
import com.elisio.sensidia.DesafioSensidia.domain.entities.FileMetadata;
import com.elisio.sensidia.DesafioSensidia.framework.adapter.out.aws.producer.SqsProducer;
import com.elisio.sensidia.DesafioSensidia.framework.adapter.out.aws.producer.S3UploadFile;
import com.elisio.sensidia.DesafioSensidia.framework.adapter.in.dto.UploadResponseDTO;
import com.elisio.sensidia.DesafioSensidia.framework.exception.ValidationParseJson;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service
@Slf4j
public class UploadPortInImpl implements UploadPortIn {

    private final SqsProducer sqsProducer;
    private final S3UploadFile s3UploadFile;

    public UploadPortInImpl(SqsProducer sqsProducer, S3UploadFile s3UploadFile) {
        this.sqsProducer = sqsProducer;
        this.s3UploadFile = s3UploadFile;
    }

    @Override
    public void uploadService(MultipartFile file, UploadResponseDTO metadata) {
        ObjectMapper objectMapper = new ObjectMapper();
        log.info("Iniciando upload service");
        validateNameFile(file, metadata);
        s3UploadFile.sendFile(file);

        try {
            String jsonMetadata = objectMapper.writeValueAsString(metadata);
            log.info("Objeto transformado: " + jsonMetadata);
            sqsProducer.sendMessage(jsonMetadata);
        } catch (JsonProcessingException e) {
            throw new ValidationParseJson(e.getOriginalMessage());
        }

    }

    private static void validateNameFile(MultipartFile file, UploadResponseDTO metadata) {
        if( !metadata.getFile().getFileName().equals(file.getOriginalFilename())){
            var newFile = new FileMetadata();
            newFile.setFileName(file.getOriginalFilename());
            newFile.setFileSize(file.getSize());
            newFile.setFileType(file.getContentType());
            metadata.setFile(newFile);

            log.info("ALterando o nome do file: " + metadata.toString());
         } else {
            log.info("Nome correto, não necessario alterar");
        }
    }
}
