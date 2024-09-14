package com.elisio.sensidia.DesafioSensidia.framework.adapter.out.aws.producer;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.PublishResult;
import com.amazonaws.services.sns.model.Topic;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class SnsReportProducer {

    private final AmazonSNS snsClient;
    Topic bookTopic;

    public SnsReportProducer(AmazonSNS amazonSNS, @Qualifier("reportEventTopic") Topic bookTopic) { //o qualifier esta no awsSns
        this.snsClient = amazonSNS;
        this.bookTopic = bookTopic;
    }

    public void publish(String message) {
        PublishResult publish = this.snsClient.publish(bookTopic.getTopicArn(), message);
        log.info("Mensagem enviada para o topico reportEventTopic com a mensagem " + message);
    }

}
