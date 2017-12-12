package agh.iosr.event.listener;

import agh.iosr.event.model.EventMessage;
import agh.iosr.handler.MessageHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class EventMessageListener implements MessageListener {

    private final MessageHandler messageHandler;

    private ObjectMapper objectMapper = new ObjectMapper();
    private Logger logger = LoggerFactory.getLogger(EventMessageListener.class);

    @Override
    public void onMessage(Message message) {

        if(!(message instanceof TextMessage)){
            return;
        }

        try {

            TextMessage textMessage = (TextMessage) message;
            EventMessage eventMessage = convertTextMessageToEventMessage(textMessage);

            logger.info("Received message from SQS");
            logger.info("Received message user id: " + eventMessage.getId());
            logger.info("Received message URL: " + eventMessage.getResourceURL());
            logger.info("Received message conversion type: " + eventMessage.getConversionType());

            messageHandler.handleMessage(eventMessage);

            message.acknowledge();
            logger.info("Acknowledged message " + message.getJMSMessageID());

        } catch (JMSException | IOException e) {
            logger.error("Error processing message: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private EventMessage convertTextMessageToEventMessage(TextMessage message) throws JMSException, IOException {
        String JSON = message.getText();
        return objectMapper.readValue(JSON, EventMessage.class);
    }

}