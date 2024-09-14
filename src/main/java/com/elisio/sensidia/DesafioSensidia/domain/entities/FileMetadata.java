package com.elisio.sensidia.DesafioSensidia.domain.entities;


import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@DynamoDBDocument
public class FileMetadata {

    @NotNull(message = "O campo fileName não pode ser null")

    private String fileName;

    @NotNull(message = "O campo fileType não pode ser null")
    private String fileType;


    @NotNull(message = "O fileSize não pode ser null")
    private Long fileSize;

    @DynamoDBAttribute(attributeName = "fileName")
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @DynamoDBAttribute(attributeName = "fileType")
    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    @DynamoDBAttribute(attributeName = "fileSize")
    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }
}
