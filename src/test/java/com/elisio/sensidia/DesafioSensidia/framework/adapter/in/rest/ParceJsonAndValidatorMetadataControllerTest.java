package com.elisio.sensidia.DesafioSensidia.framework.adapter.in.rest;

import com.elisio.sensidia.DesafioSensidia.domain.entities.FileMetadata;
import com.elisio.sensidia.DesafioSensidia.factoryMessage.FactoryMessage;
import com.elisio.sensidia.DesafioSensidia.framework.adapter.in.dto.UploadResponseDTO;
import com.elisio.sensidia.DesafioSensidia.framework.exception.AwsException;
import com.elisio.sensidia.DesafioSensidia.framework.exception.ValidationErrorException;
import com.elisio.sensidia.DesafioSensidia.framework.exception.ValidationParseJsonException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ParceJsonAndValidatorMetadataControllerTest {


    @Mock
    private Validator validator;

    @Mock
    private ConstraintViolation<UploadResponseDTO> violation;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private ParceJsonAndValidatorMetadataController validatorMetadata;


    @Test
    void parceJsonToUploadResponseDtoWithSucess() throws JsonProcessingException {
        String toString = FactoryMessage.parseUploadResponseDtoToString();
        UploadResponseDTO uploadResponseDTO = FactoryMessage.getUploadResponseDTO();
        when(objectMapper.readValue(toString, UploadResponseDTO.class)).thenReturn(uploadResponseDTO);
        UploadResponseDTO responseDTO = validatorMetadata.parceJsonToUploadResponseDto(toString);

        assertEquals(UploadResponseDTO.class, responseDTO.getClass());
        assertEquals(uploadResponseDTO.getFile().getFileName(), responseDTO.getFile().getFileName());
    }

    @Test
    void doThrwoWhenParceJsonToUploadResponseDtoSomeFieldNull() throws JsonProcessingException {

        String toString = FactoryMessage.parseUploadResponseDtoToStringWithSomeFieldNull();

        UploadResponseDTO uploadResponseDTO = FactoryMessage.getUploadResponseDTO();
        FileMetadata file = new FileMetadata();
        uploadResponseDTO.setFile(file);

        when(objectMapper.readValue(toString, UploadResponseDTO.class)).thenReturn(uploadResponseDTO);

        Set<ConstraintViolation<UploadResponseDTO>> validate = validator.validate(uploadResponseDTO);
        validate.add(violation);

        when(validator.validate(uploadResponseDTO)).thenReturn(validate);

        assertThrows(ValidationErrorException.class, () -> validatorMetadata.parceJsonToUploadResponseDto(toString));

    }

    @Test
    void doThrowObjectMapper() throws JsonProcessingException {

        String toString = FactoryMessage.parseUploadResponseDtoToStringWithSomeFieldNull();

        UploadResponseDTO uploadResponseDTO = FactoryMessage.getUploadResponseDTO();
        FileMetadata file = new FileMetadata();
        uploadResponseDTO.setFile(file);

        doThrow(JsonProcessingException.class).when(objectMapper).readValue(toString, UploadResponseDTO.class);

        assertThrows(ValidationParseJsonException.class, () -> validatorMetadata.parceJsonToUploadResponseDto(toString));
    }
}