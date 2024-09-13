package com.elisio.sensidia.DesafioSensidia.framework.AWS.config.consumer;


import com.elisio.sensidia.DesafioSensidia.framework.adapter.in.dto.UploadResponseDTO;
import com.elisio.sensidia.DesafioSensidia.framework.exception.ValidationParseJson;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Slf4j
@Component
public class SQSConsumer {

    private final S3Consumer s3Consumer;

    public SQSConsumer(S3Consumer s3Consumer) {
        this.s3Consumer = s3Consumer;
    }

    @SqsListener("sensidia-metadata")
    public void listen(String message) {

        ObjectMapper objectMapper = new ObjectMapper();

        log.info("Consumindo fila SQS com a message: " + message);

        try {
            var uploadRequestDTO = objectMapper.readValue(message, UploadResponseDTO.class);
            log.info("Mensagem transformada com sucesso para o UploadRequestDto, iniciando chamada para o S3");
            s3Consumer.downloadFileS3(uploadRequestDTO.getFile().getFileName());
        } catch (JsonProcessingException e) {
            log.error("Erro ao transformar a mensagem do SQS");
            throw new ValidationParseJson("Error to parse Json: " + e.getOriginalMessage());
        } catch (IOException e) {
            throw new ValidationParseJson("Error IOException: " + e.getMessage());
        }

    }


}
