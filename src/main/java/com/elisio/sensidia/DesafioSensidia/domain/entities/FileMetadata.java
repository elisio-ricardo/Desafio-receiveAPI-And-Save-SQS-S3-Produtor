package com.elisio.sensidia.DesafioSensidia.domain.entities;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileMetadata {

    @NotNull(message = "O campo fileName não pode ser null")
    private String fileName;

    @NotNull(message = "O campo fileType não pode ser null")
    private String fileType;


    @NotNull(message = "O fileSize não pode ser null")
    private Long fileSize;

}
