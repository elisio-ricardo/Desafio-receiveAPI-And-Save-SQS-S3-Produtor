package com.elisio.sensidia.DesafioSensidia.application.service;


import com.elisio.sensidia.DesafioSensidia.domain.entities.ProcessingResult;
import com.elisio.sensidia.DesafioSensidia.framework.adapter.in.aws.consumer.S3Consumer;
import com.elisio.sensidia.DesafioSensidia.framework.adapter.in.dto.UploadResponseDTO;
import com.elisio.sensidia.DesafioSensidia.framework.adapter.in.dto.UploadResponseDynamoDbDTO;
import com.elisio.sensidia.DesafioSensidia.framework.adapter.out.aws.producer.ProcessingDynamoDb;
import com.elisio.sensidia.DesafioSensidia.framework.adapter.out.aws.producer.SnsReportProducer;
import com.elisio.sensidia.DesafioSensidia.framework.exception.DownLoadS3Exception;
import com.elisio.sensidia.DesafioSensidia.framework.exception.UploadS3Excption;
import com.elisio.sensidia.DesafioSensidia.framework.exception.ValidationParseJson;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
public class ProcessingMessageSQS {

    private final ProcessingDynamoDb processingDynamoDb;
    private final S3Consumer s3Consumer;

    private final SnsReportProducer snsReportProducer;

    private final ObjectMapper objectMapper;


    public ProcessingMessageSQS(ProcessingDynamoDb processingDynamoDb, S3Consumer s3Consumer, SnsReportProducer snsReportProducer, ObjectMapper objectMapper) {
        this.processingDynamoDb = processingDynamoDb;
        this.s3Consumer = s3Consumer;
        this.snsReportProducer = snsReportProducer;
        this.objectMapper = objectMapper;
    }

    public void processingMessageSQS(String message) {
        log.info("Processando mensagem para ser enviada no DynamoDB: " + message);

        try {
            var uploadRequestDTO = objectMapper.readValue(message, UploadResponseDTO.class);
            log.info("Mensagem transformada com sucesso para o UploadRequestDto, iniciando chamada para o S3");
            var processingResult = getProcessingResult(uploadRequestDTO);

            if (processingResult != null) {
                sendResultMensageDynamoDB(uploadRequestDTO, processingResult);
            } else {
                throw new DownLoadS3Exception("Erro ao fazer download do arquivo no S3");
            }
        } catch (JsonProcessingException e) {
            log.error("Erro ao transformar a mensagem do SQS");
            throw new ValidationParseJson("Error to parse Json: " + e.getOriginalMessage());
        }
    }

    private void sendResultMensageDynamoDB(UploadResponseDTO uploadRequestDTO, ProcessingResult processingResult) {
        log.info("iniciando envio dos resultados para o DyanmoDB");
        var processingReportAfterSave = processingDynamoDb.processingDataSqs(uploadRequestDTO, processingResult);

        ///Por SNS aqui !
        sendREportSNS(processingReportAfterSave);
    }

    private void sendREportSNS(UploadResponseDynamoDbDTO processingReportAfterSave) {
        log.info("Iniciando envio da mensagem para o topico SNS");
        String reportString;
        try {
            reportString = objectMapper.writeValueAsString(processingReportAfterSave);
        } catch (JsonProcessingException e) {
            throw new ValidationParseJson("Erro ao transformar o report em Json antes de enviar para o SNS: " + e.getOriginalMessage());
        }
        snsReportProducer.publish(reportString);
        log.info("Report enviado com sucesso para o Topico reportProcessingFile");
    }

    private ProcessingResult getProcessingResult(UploadResponseDTO uploadRequestDTO) {
        ProcessingResult processingResult = null;
        try {
            processingResult = s3Consumer.downloadFileS3(uploadRequestDTO.getFile().getFileName());
        } catch (IOException e) {
            throw new UploadS3Excption(e.getMessage());
        }
        return processingResult;
    }
}
