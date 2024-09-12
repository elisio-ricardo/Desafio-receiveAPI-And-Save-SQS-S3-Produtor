package com.elisio.sensidia.DesafioSensidia.framework.AWS.config.consumer;


import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class SQSConsumer {

    @SqsListener("sensidia-metadata")
    public void listen(String message){
        log.info("Consumindo fila SQS");
        System.out.println("Mensagem recebida: " + message);
    }




}
