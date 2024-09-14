package com.elisio.sensidia.DesafioSensidia.framework.adapter.out.aws.producer;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


@Slf4j
@Service
public class S3UploadFile {


    @Value("${spring.cloud.aws.s3.bucket-name}")
    private String uploadSensidia;
    private final AmazonS3 amazonS3;

    public S3UploadFile(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    public PutObjectResult sendFile(MultipartFile file) {


        PutObjectResult putObjectResult = null;
        try {
            log.info("Iniciando transformação do arquivo para enviar ao S3");

            File convFile = new File(file.getOriginalFilename());
            FileOutputStream fos = new FileOutputStream(convFile);
            fos.write(file.getBytes());
            fos.close();
            var putObjectRequest = new PutObjectRequest(uploadSensidia, file.getOriginalFilename(), convFile);

            putObjectResult = amazonS3.putObject(putObjectRequest);
            log.info("Foi enviado esse arquivo: " + putObjectRequest.getFile());
            log.info("Arquivo enviado com sucesso para o Bucket S3 eTag do objeto: " + putObjectResult.getETag());

        } catch (IOException e) {
            log.error("Erro ao obter InputStream do arquivo: " + e.getMessage());
        }
        return putObjectResult;
    }
}
