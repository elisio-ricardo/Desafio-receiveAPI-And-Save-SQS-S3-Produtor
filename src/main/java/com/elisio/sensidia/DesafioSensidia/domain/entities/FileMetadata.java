package com.elisio.sensidia.DesafioSensidia.domain.entities;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
public class FileMetadata {

    @NotNull(message = "O campo fileName não pode ser null")

    private String fileName;

    @NotNull(message = "O campo fileType não pode ser null")
    private String fileType;


    @NotNull(message = "O fileSize não pode ser null")
    private Long fileSize;


    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }


    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }


    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }
}
