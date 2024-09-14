package com.elisio.sensidia.DesafioSensidia.framework.adapter.out.aws.producer;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


@Slf4j
@Service
public class S3UploadFile {

    private final AmazonS3 amazonS3;


    public S3UploadFile(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;

    }

    public void sendFile(MultipartFile file) {

        try {
            log.info("Iniciando transformação de request S3");

            File convFile = new File(file.getOriginalFilename());
            FileOutputStream fos = new FileOutputStream(convFile);
            fos.write(file.getBytes());
            fos.close();
            var putObjectRequest = new PutObjectRequest("upload-sensidia", file.getOriginalFilename(), convFile);

            amazonS3.putObject(putObjectRequest);
            log.info("Foi enviado essa request: " + putObjectRequest);
            log.info("Arquivo enviado com sucesso para o Bucket S3");

        } catch (IOException e) {
            log.error("Erro ao obter InputStream do arquivo: " + e.getMessage());
        }
    }
}
