package agh.iosr.event.listener;

import agh.iosr.event.model.EventMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.jms.TextMessage;

import java.awt.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(PowerMockRunner.class)
@PrepareForTest(EventMessage.class)
public class EventMessageConverterTest {

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private EventMessageConverter converter;


    @Test
    public void convertTextMessageToEventMessage() throws Exception {

        //given
        TextMessage message = mock(TextMessage.class);
        EventMessage eventMessage = PowerMockito.mock(EventMessage.class);
        String JSON = "{x:1,y:2}";

        //when
        when(message.getText()).thenReturn(JSON);
        when(objectMapper.readValue(JSON, EventMessage.class)).thenReturn(eventMessage);
        EventMessage result = converter.convertTextMessageToEventMessage(message);

        //then
        verify(message).getText();
        verify(objectMapper).readValue(JSON, EventMessage.class);
        assertEquals(eventMessage, result);
    }

}