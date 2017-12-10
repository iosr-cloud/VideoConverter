package agh.iosr.event.impl;

import agh.iosr.event.api.EventReceiver;
import agh.iosr.event.model.EventMessage;
import com.amazonaws.services.sqs.model.Message;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class ReceiveScheduler {

    @Autowired
    private EventReceiver eventReceiver;

    private Logger logger = LoggerFactory.getLogger(ReceiveScheduler.class);
    private ObjectMapper mapper = new ObjectMapper();

    @Scheduled(fixedDelay = 100)
    public void receiveMessage() {

        List<Message> messages = eventReceiver.receiveEventMessages();

        messages
                .stream()
                .map(Message::getBody)
                .map(this::convertJSONToMessage)
                .forEach(message -> {
                    logger.info("Received message from SQS");
                    logger.info("Received message URL: " + message.getResourceURL());
                    logger.info("Received message conversion type: " + message.getConversionType());
                });

        eventReceiver.deleteEventMessages(messages);
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
