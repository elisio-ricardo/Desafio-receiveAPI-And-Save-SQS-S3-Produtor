package com.elisio.sensidia.DesafioSensidia.framework.adapter.out.aws.producer;

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
                    log.info("Message sent successfully. Message ID: " + sendMessageResponse.messageId());
                })
                .exceptionally(throwable -> {
                    if (throwable instanceof SqsException) {
                        SqsException sqsException = (SqsException) throwable;
                        log.error("Failed to send message. Error message: " + sqsException.awsErrorDetails().errorMessage());
                    } else {
                        log.error("Unexpected error occurred: " + throwable.getMessage());
                    }
                    return null;
                });

        return sendMessageResponseCompletableFuture;
    }

}
