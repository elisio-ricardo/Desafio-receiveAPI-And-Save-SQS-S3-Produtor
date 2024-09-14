package com.elisio.sensidia.DesafioSensidia.framework.adapter.out.aws.producer;


import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.elisio.sensidia.DesafioSensidia.domain.entities.ProcessingResult;
import com.elisio.sensidia.DesafioSensidia.framework.adapter.in.dto.UploadResponseDTO;
import com.elisio.sensidia.DesafioSensidia.framework.adapter.in.dto.UploadResponseDynamoDbDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class ProcessingDynamoDb {
    private final DynamoDBMapper dbMapper;

    public ProcessingDynamoDb(DynamoDBMapper dbMapper) {
        this.dbMapper = dbMapper;
    }


    public void processingDataSqs(UploadResponseDTO uploadRequestDTO, ProcessingResult processingResult) {

        log.info("Cheguei na classe ProcessingDynamoDb ");
        var resultDetail = new UploadResponseDynamoDbDTO();
        resultDetail.setUserId(uploadRequestDTO.getUser().getEmail());
        resultDetail.setFile(uploadRequestDTO.getFile());
        resultDetail.setUser(uploadRequestDTO.getUser());
        resultDetail.setProcessingResult(processingResult);
        log.info("iniciando save no dynamoDB");
        dbMapper.save(resultDetail);

        System.out.println("Testando");
    }

}
