package com.elisio.sensidia.DesafioSensidia.framework.adapter.in.dto;


import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.elisio.sensidia.DesafioSensidia.domain.entities.FileMetadata;
import com.elisio.sensidia.DesafioSensidia.domain.entities.ProcessingResult;
import com.elisio.sensidia.DesafioSensidia.domain.entities.User;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@DynamoDBDocument
@DynamoDBTable(tableName = "result-processing-file")
public class UploadResponseDynamoDbDTO {


    @NotNull(message = "O UserEmail não pode ser null")
    @Valid
    @DynamoDBHashKey
    private String userId;

    @NotNull(message = "O Objeto User não pode ser null")
    @Valid
    @DynamoDBAttribute
    private User user;

    @NotNull(message = "O Objeto file não pode ser null")
    @Valid
    @DynamoDBAttribute
    private FileMetadata file;


    @NotNull(message = "O Objeto ProcessingResult não pode ser null")
    @Valid
    @DynamoDBAttribute
    private ProcessingResult processingResult;

}
