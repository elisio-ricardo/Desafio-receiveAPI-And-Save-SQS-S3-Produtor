package com.elisio.sensidia.DesafioSensidia.framework.adapter.in.rest;

import com.elisio.sensidia.DesafioSensidia.application.port.in.UploadPortIn;
import com.elisio.sensidia.DesafioSensidia.factoryMessage.FactoryMessage;
import com.elisio.sensidia.DesafioSensidia.framework.adapter.in.dto.UploadResponseDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UploadControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private UploadPortIn uploadPortIn;

    @Mock
    private ParceJsonAndValidatorMetadataController validatorMetadata;

    @InjectMocks
    private UploadController uploadController;

    @Test
    void uploadFileWithSucess() throws IOException {

        String uploadToString = FactoryMessage.parseUploadResponseDtoToString();
        MultipartFile file = FactoryMessage.getFile();
        UploadResponseDTO responseDTO = FactoryMessage.getUploadResponseDTO();

        when(validatorMetadata.parceJsonToUploadResponseDto(uploadToString)).thenReturn(responseDTO);

        ResponseEntity<UploadResponseDTO> responseEntity = uploadController.uploadFile(file, uploadToString);


        assertEquals(200, responseEntity.getStatusCode().value());
        assertEquals("user123", responseEntity.getBody().getUser().getUserId());
        assertEquals("textfile.txt", responseEntity.getBody().getFile().getFileName());
    }

}