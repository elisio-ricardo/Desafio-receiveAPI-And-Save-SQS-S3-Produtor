package com.elisio.sensidia.DesafioSensidia.framework.adapter.out.aws.producer;


import com.elisio.sensidia.DesafioSensidia.factoryMessage.FactoryMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageResponse;

import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class SqsProducerTest {


    @Mock
    private SqsAsyncClient sqsAsyncClient;

    @InjectMocks
    private SqsProducer sqsProducer;

    @BeforeEach
    public void setUp() {

        ReflectionTestUtils.setField(sqsProducer, "uploadQueueARN",
                        "arn:aws:sns:us-east-2:980921746480:reportProcessingFile");
    }

    @Test
    void sendMessageWithSuccess() {

        String toString = FactoryMessage.parseUploadResponseDtoToString();
        SendMessageRequest sendMessageRequest = SendMessageRequest.builder()
                .queueUrl("arn:aws:sns:us-east-2:980921746480:reportProcessingFile")
                .messageBody(toString)
                .build();
        SendMessageResponse sendMessageResponse = SendMessageResponse.builder()
                .messageId("12345")
                .build();

        CompletableFuture<SendMessageResponse> futureResponse = CompletableFuture.completedFuture(sendMessageResponse);

        when(sqsAsyncClient.sendMessage(sendMessageRequest)).thenReturn(futureResponse);

        CompletableFuture<SendMessageResponse> completableFuture = sqsProducer.sendMessage(toString);

        assertNotNull(completableFuture);
    }
}