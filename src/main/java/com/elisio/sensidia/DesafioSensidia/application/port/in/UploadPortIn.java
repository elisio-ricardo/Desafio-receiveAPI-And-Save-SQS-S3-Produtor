package com.elisio.sensidia.DesafioSensidia.application.port.in;


import com.elisio.sensidia.DesafioSensidia.domain.entities.Upload;
import com.elisio.sensidia.DesafioSensidia.framework.adapter.in.dto.UploadResponseDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface UploadPortIn {

    UploadResponseDTO saveUpload(MultipartFile file, Upload metadata);
}
