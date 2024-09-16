package com.elisio.sensidia.DesafioSensidia.application.service;

import com.amazonaws.services.s3.model.PutObjectResult;
import com.elisio.sensidia.DesafioSensidia.application.port.in.UploadPortIn;
import com.elisio.sensidia.DesafioSensidia.domain.entities.FileMetadata;
import com.elisio.sensidia.DesafioSensidia.framework.adapter.in.dto.UploadResponseDTO;
import com.elisio.sensidia.DesafioSensidia.framework.adapter.out.aws.producer.S3UploadFile;
import com.elisio.sensidia.DesafioSensidia.framework.adapter.out.aws.producer.SqsProducer;
import com.elisio.sensidia.DesafioSensidia.framework.exception.AwsException;
import com.elisio.sensidia.DesafioSensidia.framework.exception.ValidationParseJsonException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service
@Slf4j
public class UploadPortInImplService implements UploadPortIn {

    private final SqsProducer sqsProducer;
    private final S3UploadFile s3UploadFile;

    public UploadPortInImplService(SqsProducer sqsProducer, S3UploadFile s3UploadFile) {
        this.sqsProducer = sqsProducer;
        this.s3UploadFile = s3UploadFile;
    }

    @Override
    public void uploadService(MultipartFile file, UploadResponseDTO metadata) {
        log.info(file.toString());
        ObjectMapper objectMapper = new ObjectMapper();
        log.info("Iniciando upload service");
        validateFile(file, metadata);
        PutObjectResult putObjectResult = s3UploadFile.sendFile(file);


        if (putObjectResult != null) {
            log.info("Arquivo enviado ao S3 com sucesso");
            try {
                String jsonMetadata = objectMapper.writeValueAsString(metadata);
                log.info("Objeto transformado: " + jsonMetadata);
                sqsProducer.sendMessage(jsonMetadata);
            } catch (JsonProcessingException e) {
                throw new ValidationParseJsonException(e.getOriginalMessage());
            }
        } else {
            throw new AwsException("Erro ao enviar o arquivo para o Bucket S3");
        }
        log.info("Arquivo enviado ao S3 e mensagem ao SQS com sucesso");

    }

    private void validateFile(MultipartFile file, UploadResponseDTO metadata) {
        var newFile = new FileMetadata();
        newFile.setFileName(metadata.getFile().getFileName());
        newFile.setFileSize(file.getSize() != metadata.getFile().getFileSize() ? file.getSize() : metadata.getFile().getFileSize());
        newFile.setFileType(file.getContentType() != metadata.getFile().getFileType() ? file.getContentType() : metadata.getFile().getFileType());
        metadata.setFile(newFile);

        log.info("Conferindo Metadados do file: " + metadata.toString());

    }
}
