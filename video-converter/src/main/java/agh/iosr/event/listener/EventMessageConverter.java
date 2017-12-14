package agh.iosr.event.listener;

import agh.iosr.event.model.EventMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.TextMessage;
import java.io.IOException;

@Component
@AllArgsConstructor
public class EventMessageConverter {

    private ObjectMapper mapper;

    public EventMessage convertTextMessageToEventMessage(TextMessage message) throws JMSException, IOException {
        String JSON = message.getText();
        return mapper.readValue(JSON, EventMessage.class);
    }

}
