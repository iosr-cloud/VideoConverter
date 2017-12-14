package agh.iosr.event.listener;

import agh.iosr.event.model.EventMessage;
import agh.iosr.handler.MessageHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class EventMessageListener implements MessageListener {

    private final MessageHandler messageHandler;
    private final EventMessageConverter converter;

    @Override
    public void onMessage(Message message) {

        if(!(message instanceof TextMessage)){
            return;
        }

        try {

            TextMessage textMessage = (TextMessage) message;
            EventMessage eventMessage = converter.convertTextMessageToEventMessage(textMessage);

            log.info("Received message from SQS");
            log.info("Received message user id: " + eventMessage.getId());
            log.info("Received message URL: " + eventMessage.getResourceURL());
            log.info("Received message conversion type: " + eventMessage.getConversionType());

            messageHandler.handleMessage(eventMessage);

            message.acknowledge();
            log.info("Acknowledged message " + message.getJMSMessageID());

        } catch (JMSException | IOException e) {
            log.error("Error processing message: " + e.getMessage());
            e.printStackTrace();
        }
    }

}