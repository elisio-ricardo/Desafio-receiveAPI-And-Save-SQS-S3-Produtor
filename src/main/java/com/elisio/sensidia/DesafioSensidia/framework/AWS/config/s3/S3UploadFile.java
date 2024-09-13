package com.elisio.sensidia.DesafioSensidia.framework.AWS.config.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;


@Slf4j
@Service
public class S3UploadFile {

    private final AmazonS3 amazonS3;


    public S3UploadFile(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;

    }

    public void sendFile(MultipartFile file) throws IOException {

        try {
            log.info("Preparando arquivo para Bucket S3");
            InputStream inputStream = file.getInputStream();
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(file.getOriginalFilename());
            metadata.setContentLength(file.getSize());

            PutObjectRequest putObjectRequest = new PutObjectRequest("upload-sensidia", file.getOriginalFilename(), inputStream, metadata);
            PutObjectResult putObjectResult = amazonS3.putObject(putObjectRequest);
            log.info(putObjectResult.toString());
            log.info("Arquivo enviado com sucesso para o Bucket S3");

        } catch (IOException e) {
            log.error("Erro ao obter InputStream do arquivo: " + e.getMessage());
        }
    }
}
