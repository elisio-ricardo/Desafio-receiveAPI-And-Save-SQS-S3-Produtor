package com.elisio.sensidia.DesafioSensidia.application.service;

import com.elisio.sensidia.DesafioSensidia.application.port.in.UploadPortIn;
import com.elisio.sensidia.DesafioSensidia.domain.entities.Upload;
import com.elisio.sensidia.DesafioSensidia.framework.AWS.config.producer.SqsProducer;
import com.elisio.sensidia.DesafioSensidia.framework.AWS.config.s3.S3UploadFile;
import com.elisio.sensidia.DesafioSensidia.framework.adapter.in.dto.UploadResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


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
    public UploadResponseDTO uploadSQSAndS3(MultipartFile file, Upload metadata) {

        try {
            s3UploadFile.sendFile(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        sqsProducer.sendMessage(metadata.toString());


        return null;
    }
}
