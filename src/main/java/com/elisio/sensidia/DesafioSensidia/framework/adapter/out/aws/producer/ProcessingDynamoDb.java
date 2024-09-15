package com.elisio.sensidia.DesafioSensidia.framework.adapter.out.aws.producer;


import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.elisio.sensidia.DesafioSensidia.domain.entities.ProcessingResult;
import com.elisio.sensidia.DesafioSensidia.framework.adapter.in.dto.UploadResponseDTO;
import com.elisio.sensidia.DesafioSensidia.framework.adapter.in.dto.UploadResponseDynamoDbDTO;
import com.elisio.sensidia.DesafioSensidia.framework.exception.ValidateNullPointerException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class ProcessingDynamoDb {
    private final DynamoDBMapper dbMapper;

    public ProcessingDynamoDb(DynamoDBMapper dbMapper) {
        this.dbMapper = dbMapper;
    }


    public UploadResponseDynamoDbDTO processingDataSqs(UploadResponseDTO responseDTO, ProcessingResult processingResult) {

        log.info("Cheguei na classe ProcessingDynamoDb ");
        try {
            var resultDetail = new UploadResponseDynamoDbDTO();
            resultDetail.setUserId(responseDTO.getUser().getUserId());
            resultDetail.setFileName(responseDTO.getFile().getFileName());
            resultDetail.setFileSize(responseDTO.getFile().getFileSize());
            resultDetail.setProcessingResult(processingResult.getQtdLinhas());
            resultDetail.setStatus(processingResult.getStatus());
            log.info("iniciando save no dynamoDB");
            dbMapper.save(resultDetail);

            log.info("Mensagem enviada com sucesso: " + resultDetail.getFileId());
            return resultDetail;
        } catch (NullPointerException e) {
            throw new ValidateNullPointerException("Erro ao criar report, valores nullos: " + e.getMessage());
        }
    }
}
