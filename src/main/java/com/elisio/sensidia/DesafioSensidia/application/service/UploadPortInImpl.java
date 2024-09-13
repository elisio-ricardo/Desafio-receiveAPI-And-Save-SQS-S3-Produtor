package com.elisio.sensidia.DesafioSensidia.application.service;

import com.elisio.sensidia.DesafioSensidia.application.port.in.UploadPortIn;
import com.elisio.sensidia.DesafioSensidia.domain.entities.FileMetadata;
import com.elisio.sensidia.DesafioSensidia.domain.entities.Upload;
import com.elisio.sensidia.DesafioSensidia.framework.AWS.config.producer.SqsProducer;
import com.elisio.sensidia.DesafioSensidia.framework.AWS.config.s3.S3UploadFile;
import com.elisio.sensidia.DesafioSensidia.framework.adapter.in.dto.UploadResponseDTO;
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
        log.info("Iniciando upload service");
        validateNameFile(file, metadata);
        s3UploadFile.sendFile(file);
        sqsProducer.sendMessage(metadata.toString());
    }

    private static void validateNameFile(MultipartFile file, UploadResponseDTO metadata) {
        if( !metadata.getFile().getFileName().equals(file.getOriginalFilename())){
            var newFileMetadata = new FileMetadata();
            newFileMetadata.setFileName(file.getOriginalFilename());
            newFileMetadata.setFileSize(metadata.getFile().getFileSize());
            newFileMetadata.setFileType(file.getContentType());
            metadata.setFile(newFileMetadata);
            log.info("ALterando o nome do file: " + metadata.toString());
         } else {
            log.info("Nome correto, não necessario alterar");
        }
    }
}
