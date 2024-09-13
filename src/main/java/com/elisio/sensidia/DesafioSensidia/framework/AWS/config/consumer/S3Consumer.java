package com.elisio.sensidia.DesafioSensidia.framework.AWS.config.consumer;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.elisio.sensidia.DesafioSensidia.framework.exception.DownLoadS3Exception;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@Slf4j
@Service
public class S3Consumer {

    @Value("${spring.cloud.aws.s3.bucket-name}")
    private String bucketName;
    private final AmazonS3 s3Client;

    public S3Consumer(AmazonS3 s3Client) {
        this.s3Client = s3Client;
    }

    public void downloadFileS3(String key) throws IOException {

        try {
            log.info("Iniciando Download do file no S3");
            S3Object s3Object = s3Client.getObject(new GetObjectRequest(bucketName, key));

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(s3Object.getObjectContent(), StandardCharsets.UTF_8));

            String line;
            int lineCount = 0;

            while ((line = reader.readLine()) != null) {
                lineCount++;
            }

            log.info("Número total de linhas: " + lineCount);
            reader.close();
            s3Object.close();

        } catch (Exception e) {
            log.error("Erro ao tentar fazer download no s3");
            throw new DownLoadS3Exception("Error to Download file to S3: " + e.getMessage());
        }
    }
}
