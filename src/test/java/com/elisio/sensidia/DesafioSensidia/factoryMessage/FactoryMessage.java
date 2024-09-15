package com.elisio.sensidia.DesafioSensidia.factoryMessage;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.elisio.sensidia.DesafioSensidia.domain.entities.FileMetadata;
import com.elisio.sensidia.DesafioSensidia.domain.entities.ProcessingResult;
import com.elisio.sensidia.DesafioSensidia.domain.entities.User;
import com.elisio.sensidia.DesafioSensidia.domain.enums.ResultEnum;
import com.elisio.sensidia.DesafioSensidia.framework.adapter.in.dto.UploadResponseDTO;
import com.elisio.sensidia.DesafioSensidia.framework.adapter.in.dto.UploadResponseDynamoDbDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;

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


    public static String parseUploadResponseDtoToStringWithSomeFieldNull() {
        ObjectMapper mapper = new ObjectMapper();

        var uploadResponseDTO = getUploadResponseDTO();
        FileMetadata file = new FileMetadata();
        uploadResponseDTO.setFile(file);

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
        fileMetadata.setFileName("textfile.txt");
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

    public static MultipartFile getFile(){
        byte[] content = "Este é o conteúdo do arquivo".getBytes();

        MultipartFile file =
                new MockMultipartFile("file","textfile.txt","text/plain", content );

        return file;
    }

    public static PutObjectResult getPutObjectResult() {
        PutObjectResult putObjectResult = new PutObjectResult();

        putObjectResult.setETag("123456789abcdef");
        putObjectResult.setVersionId("1");
        putObjectResult.setExpirationTime(null);
        putObjectResult.setContentMd5("abc123def456ghi789");

        return putObjectResult;
    }


    public static S3Object getS3Object() {
        S3Object s3Object = new S3Object();

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(1024);
        metadata.setContentType("application/pdf");


        String fileContent = "Este é o conteúdo do arquivo.";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(fileContent.getBytes());

        s3Object.setObjectMetadata(metadata);
        s3Object.setObjectContent(inputStream);

        s3Object.setKey("teste.pdf");
        s3Object.setBucketName("bucket-de-teste");

        return s3Object;
    }

}
