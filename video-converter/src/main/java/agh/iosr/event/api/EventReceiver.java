package agh.iosr.event.api;

import com.amazonaws.services.sqs.model.Message;

import java.util.List;

public interface EventReceiver {

    List<Message> receiveEventMessages();

    void deleteEventMessages(List<Message> messages);
}
