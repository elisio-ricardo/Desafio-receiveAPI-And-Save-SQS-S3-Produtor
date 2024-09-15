package com.elisio.sensidia.DesafioSensidia.framework.adapter.in.aws.consumer;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.elisio.sensidia.DesafioSensidia.domain.entities.ProcessingResult;
import com.elisio.sensidia.DesafioSensidia.domain.enums.ResultEnum;
import com.elisio.sensidia.DesafioSensidia.factoryMessage.FactoryMessage;
import com.elisio.sensidia.DesafioSensidia.framework.exception.AwsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class S3ConsumerTest {


    @Mock
    private AmazonS3 s3Client;

    @InjectMocks
    private S3Consumer s3Consumer;

    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(s3Consumer, "bucketName", "upload-sensidia");
    }

    @Test
    void downloadFileS3() {

        S3Object s3Object = FactoryMessage.getS3Object();
        String key = "teste.pdf";
        when(s3Client.getObject(new GetObjectRequest("upload-sensidia", key))).thenReturn(s3Object);

        ProcessingResult result = s3Consumer.downloadFileS3(key);

        assertEquals(ResultEnum.CONCLUIDO, result.getStatus());
        assertEquals(1L, result.getQtdLinhas());

    }

    @Test
    void errorWhendownloadFileS3() {
        String key = null;

        assertThrows(AwsException.class, () -> s3Consumer.downloadFileS3(key));

        verify(s3Client, Mockito.times(1)).getObject(new GetObjectRequest("upload-sensidia", null));
    }
}