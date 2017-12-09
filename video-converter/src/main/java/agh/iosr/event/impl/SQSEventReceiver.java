package agh.iosr.event.impl;

import agh.iosr.event.api.EventReceiver;
import agh.iosr.event.model.EventMessage;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class SQSEventReceiver implements EventReceiver {

    private Logger logger = LoggerFactory.getLogger(SQSEventReceiver.class);
    private ObjectMapper mapper = new ObjectMapper();

    @Value("${aws.queue.name}")
    private String queueName;
    private AmazonSQS amazonSQS;

    public SQSEventReceiver(AmazonSQS amazonSQS) {
        this.amazonSQS = amazonSQS;
    }

    @Override
    public EventMessage receiveEvent() {

        ReceiveMessageRequest messageRequest = createReceiveMessageRequest(queueName);

        EventMessage eventMessage = amazonSQS
                .receiveMessage(messageRequest)
                .getMessages()
                .stream()
                .map(message -> message.getBody())
                .map(this::convertJSONToMessage)
                .findFirst().get();

        logger.info("Received message from SQS");
        logger.info("Received message URL: " + eventMessage.getResourceURL());
        logger.info("Received message conversion type: " + eventMessage.getConversionType());
        return eventMessage;
    }

    private ReceiveMessageRequest createReceiveMessageRequest(String queueName) {

        String queueUrl = amazonSQS.getQueueUrl(queueName).getQueueUrl();
        return new ReceiveMessageRequest(queueUrl);
    }

    //todo better exception handling
    private EventMessage convertJSONToMessage(String message) {
        try {
            return mapper.readValue(message, EventMessage.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
