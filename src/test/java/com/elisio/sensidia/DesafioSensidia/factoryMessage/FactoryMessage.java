package com.elisio.sensidia.DesafioSensidia.factoryMessage;

import com.elisio.sensidia.DesafioSensidia.domain.entities.FileMetadata;
import com.elisio.sensidia.DesafioSensidia.domain.entities.ProcessingResult;
import com.elisio.sensidia.DesafioSensidia.domain.entities.User;
import com.elisio.sensidia.DesafioSensidia.domain.enums.ResultEnum;
import com.elisio.sensidia.DesafioSensidia.framework.adapter.in.dto.UploadResponseDTO;
import com.elisio.sensidia.DesafioSensidia.framework.adapter.in.dto.UploadResponseDynamoDbDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class FactoryMessage {


    public static String parseUploadResponseDtoToString() {
        ObjectMapper mapper = new ObjectMapper();

        var uploadResponseDTO = getUploadResponseDTO();

        try {
            return mapper.writeValueAsString(uploadResponseDTO);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static ProcessingResult getProcessingResult() {
        ProcessingResult result = new ProcessingResult();
        result.setQtdLinhas(10L);
        result.setStatus(ResultEnum.CONCLUIDO);

        return result;
    }

    public static UploadResponseDTO getUploadResponseDTO() {
        UploadResponseDTO uploadResponseDTO = new UploadResponseDTO();
        User user = new User();
        FileMetadata fileMetadata = new FileMetadata();

        user.setUserId("user123");
        user.setEmail("user5@example.com");
        fileMetadata.setFileName("DesafioSensedia-ServiçodeGestãodeProcessamentodeArquivos.pdf");
        fileMetadata.setFileType("application/pdf");
        fileMetadata.setFileSize(52728L);
        uploadResponseDTO.setFile(fileMetadata);
        uploadResponseDTO.setUser(user);

        return uploadResponseDTO;
    }

    public static UploadResponseDynamoDbDTO getUploadResponseDynamoDbDTO() {

        var responseDTO = getUploadResponseDTO();
        var processingResult = getProcessingResult();

        var resultDetail = new UploadResponseDynamoDbDTO();
        resultDetail.setFileId("9de3696d-d2bf-4def-9207-6a6f3560e418");
        resultDetail.setUserId(responseDTO.getUser().getUserId());
        resultDetail.setFileName(responseDTO.getFile().getFileName());
        resultDetail.setFileSize(responseDTO.getFile().getFileSize());
        resultDetail.setProcessingResult(processingResult.getQtdLinhas());
        resultDetail.setStatus(processingResult.getStatus());

        return resultDetail;
    }

}
