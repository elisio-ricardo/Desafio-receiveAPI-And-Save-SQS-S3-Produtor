package com.elisio.sensidia.DesafioSensidia.framework.adapter.out.aws.producer;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.elisio.sensidia.DesafioSensidia.domain.entities.FileMetadata;
import com.elisio.sensidia.DesafioSensidia.domain.entities.ProcessingResult;
import com.elisio.sensidia.DesafioSensidia.factoryMessage.FactoryMessage;
import com.elisio.sensidia.DesafioSensidia.framework.adapter.in.dto.UploadResponseDTO;
import com.elisio.sensidia.DesafioSensidia.framework.adapter.in.dto.UploadResponseDynamoDbDTO;
import com.elisio.sensidia.DesafioSensidia.framework.exception.ValidateNullPointerException;
import com.elisio.sensidia.DesafioSensidia.framework.exception.ValidationErrorException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class ProcessingDynamoDbTest {

    @Mock
    private DynamoDBMapper dbMapper;


    @InjectMocks
    private ProcessingDynamoDb processingDynamoDb;

    @Test
    void processingDataSqsWithSuccess() {

        ProcessingResult processingResult = FactoryMessage.getProcessingResult();
        UploadResponseDTO uploadResponseDTO = FactoryMessage.getUploadResponseDTO();

        var responseDynamoDbDTO = processingDynamoDb.processingDataSqs(uploadResponseDTO, processingResult);

        assertEquals(processingResult.getStatus(), responseDynamoDbDTO.getStatus());
        verify(dbMapper, Mockito.times(1)).save(responseDynamoDbDTO);
    }

    @Test
    void doThrowNullPointerExcepitionWhenSomeFieldNull() {

        ProcessingResult processingResult = FactoryMessage.getProcessingResult();
        UploadResponseDTO uploadResponseDTO = FactoryMessage.getUploadResponseDTO();
        var fileNull = new FileMetadata();
        uploadResponseDTO.setFile(fileNull);



        assertThrows(ValidateNullPointerException.class, () -> processingDynamoDb.processingDataSqs(uploadResponseDTO, processingResult));
        verify(dbMapper, Mockito.times(0)).save(any());
    }
}