package agh.iosr.event.listener;

import agh.iosr.event.model.EventMessage;
import agh.iosr.handler.MessageHandler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(EventMessage.class)
public class EventMessageListenerTest {

    @Mock
    private MessageHandler handler;

    @Mock
    private EventMessageConverter converter;

    @InjectMocks
    private EventMessageListener eventMessageListener;

    @Test
    public void onMessage() throws Exception {

        //given
        TextMessage message = mock(TextMessage.class);
        EventMessage eventMessage = PowerMockito.mock(EventMessage.class);

        //when
        when(converter.convertTextMessageToEventMessage(message)).thenReturn(eventMessage);
        eventMessageListener.onMessage(message);

        verify(handler).handleMessage(eventMessage);
    }

}