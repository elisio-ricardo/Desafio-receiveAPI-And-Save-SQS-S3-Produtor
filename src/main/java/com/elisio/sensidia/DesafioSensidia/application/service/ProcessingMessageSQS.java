package com.elisio.sensidia.DesafioSensidia.application.service;


import com.elisio.sensidia.DesafioSensidia.domain.entities.ProcessingResult;
import com.elisio.sensidia.DesafioSensidia.framework.adapter.in.aws.consumer.S3Consumer;
import com.elisio.sensidia.DesafioSensidia.framework.adapter.in.dto.UploadResponseDTO;
import com.elisio.sensidia.DesafioSensidia.framework.adapter.in.dto.UploadResponseDynamoDbDTO;
import com.elisio.sensidia.DesafioSensidia.framework.adapter.out.aws.producer.ProcessingDynamoDb;
import com.elisio.sensidia.DesafioSensidia.framework.adapter.out.aws.producer.SnsReportProducer;
import com.elisio.sensidia.DesafioSensidia.framework.exception.AwsException;
import com.elisio.sensidia.DesafioSensidia.framework.exception.ValidationParseJsonException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
            var uploadResponseDTO = objectMapper.readValue(message, UploadResponseDTO.class);
            log.info("Mensagem transformada com sucesso para o UploadRequestDto, iniciando chamada para o S3");
            var processingResult = getProcessingResult(uploadResponseDTO);

            sendResultMensageDynamoDB(uploadResponseDTO, processingResult);

        } catch (JsonProcessingException e) {
            log.error("Erro ao transformar a mensagem do SQS");
            throw new ValidationParseJsonException("Error to parse Json: " + e.getOriginalMessage());
        }
    }

    private ProcessingResult getProcessingResult(UploadResponseDTO responseDTO) {
        ProcessingResult processingResult = null;
        if (responseDTO.getFile() != null && responseDTO.getFile().getFileName() != null) {
            processingResult = s3Consumer.downloadFileS3(responseDTO.getFile().getFileName());
        } else {
            throw new AwsException("Erro ao fazer download no S3, File name Nullo");
        }
        return processingResult;
    }

    private void sendResultMensageDynamoDB(UploadResponseDTO uploadRequestDTO, ProcessingResult processingResult) {
        log.info("iniciando envio dos resultados para o DyanmoDB");
        var processingReportAfterSave = processingDynamoDb.processingDataSqs(uploadRequestDTO, processingResult);

        if (processingReportAfterSave.getFileId() != null) {
            sendReportSNS(processingReportAfterSave);
        } else {
            throw new AwsException("Error ao salvar Report, não foi salvo");
        }
    }

    private void sendReportSNS(UploadResponseDynamoDbDTO processingReportAfterSave) {
        log.info("Iniciando envio da mensagem para o topico SNS");
        String reportString;
        try {
            reportString = objectMapper.writeValueAsString(processingReportAfterSave);
        } catch (JsonProcessingException e) {
            throw new ValidationParseJsonException("Erro ao transformar o report em Json antes de enviar para o SNS: " + e.getOriginalMessage());
        }
        snsReportProducer.publish(reportString);
        log.info("Report enviado com sucesso para o Topico reportProcessingFile");
    }
}
