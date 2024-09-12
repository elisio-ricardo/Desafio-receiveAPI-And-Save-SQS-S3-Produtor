package com.elisio.sensidia.DesafioSensidia.application.service;

import com.elisio.sensidia.DesafioSensidia.application.port.in.UploadPortIn;
import com.elisio.sensidia.DesafioSensidia.domain.entities.Upload;
import com.elisio.sensidia.DesafioSensidia.framework.AWS.config.producer.SqsProducer;
import com.elisio.sensidia.DesafioSensidia.framework.adapter.in.dto.UploadResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.sqs.model.SendMessageResponse;

import java.util.concurrent.CompletableFuture;


@Service
@Slf4j
public class UploadPortInImpl implements UploadPortIn {

    private final SqsProducer sqsProducer;

    public UploadPortInImpl(SqsProducer sqsProducer) {
        this.sqsProducer = sqsProducer;
    }

    @Override
    public UploadResponseDTO uploadSQSAndS3(MultipartFile file, Upload metadata) {

       sqsProducer.sendMessage(metadata.toString());

        return null;
    }
}
