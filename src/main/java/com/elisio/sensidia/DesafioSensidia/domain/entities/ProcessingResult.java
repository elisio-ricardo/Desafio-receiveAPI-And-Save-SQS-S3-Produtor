package com.elisio.sensidia.DesafioSensidia.domain.entities;


import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@DynamoDBDocument
public class ProcessingResult {


    @NotNull(message = "O qtdLinhas não pode ser null")
    private Long qtdLinhas;

    @NotNull(message = "O status não pode ser null")
    private String status;
}
