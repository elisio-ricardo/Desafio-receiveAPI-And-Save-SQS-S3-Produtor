package com.elisio.sensidia.DesafioSensidia.application.service;

import com.amazonaws.services.s3.model.PutObjectResult;
import com.elisio.sensidia.DesafioSensidia.domain.entities.FileMetadata;
import com.elisio.sensidia.DesafioSensidia.factoryMessage.FactoryMessage;
import com.elisio.sensidia.DesafioSensidia.framework.adapter.in.dto.UploadResponseDTO;
import com.elisio.sensidia.DesafioSensidia.framework.adapter.out.aws.producer.S3UploadFile;
import com.elisio.sensidia.DesafioSensidia.framework.adapter.out.aws.producer.SqsProducer;
import com.elisio.sensidia.DesafioSensidia.framework.exception.AwsException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class UploadPortInImplServiceTest {

    @Mock
    private SqsProducer sqsProducer;

    @Mock
    private S3UploadFile s3UploadFile;


    @InjectMocks
    private UploadPortInImplService uploadPortInImplService;

    @Test
    @DisplayName("upload File S3A nd Send Message SQS With Sucess")
    void uploadFileS3AndSendMessageSQSWithSucess() {

        UploadResponseDTO uploadResponseDTO = FactoryMessage.getUploadResponseDTO();
        MultipartFile file = FactoryMessage.getFile();
        PutObjectResult putObjectResult = FactoryMessage.getPutObjectResult();

        when(s3UploadFile.sendFile(file)).thenReturn(putObjectResult);

        uploadPortInImplService.uploadService(file, uploadResponseDTO);

        verify(s3UploadFile, Mockito.times(1)).sendFile(file);
        verify(sqsProducer, Mockito.times(1)).sendMessage(anyString());
    }

    @Test
    @DisplayName("FileName equals metadata")
    void validateNameWithNameUploadEqualsFile() {

        UploadResponseDTO uploadResponseDTO = FactoryMessage.getUploadResponseDTO();
        MultipartFile file = FactoryMessage.getFile();
        PutObjectResult putObjectResult = FactoryMessage.getPutObjectResult();
        FileMetadata newFile = new FileMetadata();
        newFile.setFileName("textfile.txt");
        uploadResponseDTO.setFile(newFile);

        when(s3UploadFile.sendFile(file)).thenReturn(putObjectResult);

        uploadPortInImplService.uploadService(file, uploadResponseDTO);

        verify(s3UploadFile, Mockito.times(1)).sendFile(file);
        verify(sqsProducer, Mockito.times(1)).sendMessage(anyString());
    }

    @Test
    @DisplayName("ObjectResult null")
    void doThrowWhenPutObjectResultIsNull() {

        UploadResponseDTO uploadResponseDTO = FactoryMessage.getUploadResponseDTO();
        MultipartFile file = FactoryMessage.getFile();
        PutObjectResult putObjectResult = null;
        FileMetadata newFile = new FileMetadata();
        newFile.setFileName("textfile.txt");
        uploadResponseDTO.setFile(newFile);

        when(s3UploadFile.sendFile(file)).thenReturn(putObjectResult);

        assertThrows(AwsException.class, () -> uploadPortInImplService.uploadService(file, uploadResponseDTO));

        verify(s3UploadFile, Mockito.times(1)).sendFile(file);
        verify(sqsProducer, Mockito.times(0)).sendMessage(anyString());
    }

}