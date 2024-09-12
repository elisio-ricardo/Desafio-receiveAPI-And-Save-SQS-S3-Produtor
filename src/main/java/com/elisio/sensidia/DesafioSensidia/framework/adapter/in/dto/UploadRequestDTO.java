package com.elisio.sensidia.DesafioSensidia.framework.adapter.in.dto;

import com.elisio.sensidia.DesafioSensidia.domain.entities.FileMetadata;
import com.elisio.sensidia.DesafioSensidia.domain.entities.User;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class UploadRequestDTO {

    @NotNull
    private User user;

    @NotNull
    private FileMetadata file;
}
