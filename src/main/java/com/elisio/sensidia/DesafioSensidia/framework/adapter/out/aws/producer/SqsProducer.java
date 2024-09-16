package com.elisio.sensidia.DesafioSensidia.framework.adapter.out.aws.producer;

import com.elisio.sensidia.DesafioSensidia.domain.entities.FileMetadata;
import com.elisio.sensidia.DesafioSensidia.domain.entities.User;
import com.elisio.sensidia.DesafioSensidia.framework.adapter.in.dto.UploadResponseDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageResponse;
import software.amazon.awssdk.services.sqs.model.SqsException;

import java.util.concurrent.CompletableFuture;


@Slf4j
@Service
public class SqsProducer {

    @Value("${spring.cloud.aws.endpoint}")
    private String uploadQueueARN;

    private final SqsAsyncClient sqsAsyncClient;

    public SqsProducer( SqsAsyncClient sqsAsyncClient) {
        this.sqsAsyncClient = sqsAsyncClient;
    }


    public CompletableFuture<SendMessageResponse> sendMessage(String message) {

        SendMessageRequest sendMessageRequest = SendMessageRequest.builder()
                .queueUrl(uploadQueueARN)
                .messageBody(message)
                .build();

        CompletableFuture<SendMessageResponse> sendMessageResponseCompletableFuture = sqsAsyncClient.sendMessage(sendMessageRequest);

        sendMessageResponseCompletableFuture
                .thenAccept(sendMessageResponse -> {
                    log.info("Mensagem enviada com sucesso. ID da mensagem: " + sendMessageResponse.messageId());
                })
                .exceptionally(throwable -> {
                    if (throwable instanceof SqsException) {
                        SqsException sqsException = (SqsException) throwable;
                        log.error("Falha ao enviar mensagem. Mensagem de erro: " + sqsException.awsErrorDetails().errorMessage());
                    } else {
                        log.error("Ocorreu um erro inesperado: " + throwable.getMessage());
                    }
                    return null;
                });

        return sendMessageResponseCompletableFuture;
    }
//
//    public static String parseUploadResponseDtoToStringWithSomeFieldNull() {
//        ObjectMapper mapper = new ObjectMapper();
//
//        var uploadResponseDTO = getUploadResponseDTO();
//        FileMetadata file = new FileMetadata();
//        uploadResponseDTO.setFile(file);
//
//        try {
//            return mapper.writeValueAsString(uploadResponseDTO);
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    public static UploadResponseDTO getUploadResponseDTO() {
//        UploadResponseDTO uploadResponseDTO = new UploadResponseDTO();
//        User user = new User();
//        FileMetadata fileMetadata = new FileMetadata();
//
//        user.setUserId("user123");
//        user.setEmail("user5@example.com");
//        fileMetadata.setFileName("textfile.txt");
//        fileMetadata.setFileType("application/pdf");
//        fileMetadata.setFileSize(52728L);
//        uploadResponseDTO.setFile(fileMetadata);
//        uploadResponseDTO.setUser(user);
//
//        return uploadResponseDTO;
//    }


}
