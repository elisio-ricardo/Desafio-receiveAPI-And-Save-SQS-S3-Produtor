package com.elisio.sensidia.DesafioSensidia.application.service;

import com.elisio.sensidia.DesafioSensidia.application.port.in.UploadPortIn;
import com.elisio.sensidia.DesafioSensidia.domain.entities.Upload;
import com.elisio.sensidia.DesafioSensidia.framework.adapter.in.dto.UploadResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service
@Slf4j
public class UploadPortInImpl implements UploadPortIn {
    @Override
    public UploadResponseDTO uploadSQSAndS3(MultipartFile file, Upload metadata) {
        return null;
    }
}
