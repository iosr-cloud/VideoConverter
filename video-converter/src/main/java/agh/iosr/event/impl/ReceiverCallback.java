package agh.iosr.event.impl;

import agh.iosr.handler.MessageHandler;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import java.util.concurrent.TimeUnit;

public class ReceiverCallback implements MessageListener {
    // Used to listen for message silence
    private volatile long timeOfLastMessage = System.nanoTime();

    @Autowired
    private MessageHandler messageHandler;

    public void waitForOneMinuteOfSilence() throws InterruptedException {
        for (; ; ) {
            long timeSinceLastMessage = System.nanoTime() - timeOfLastMessage;
            long remainingTillOneMinuteOfSilence =
                    TimeUnit.MINUTES.toNanos(1) - timeSinceLastMessage;
            if (remainingTillOneMinuteOfSilence < 0) {
                break;
            }
            TimeUnit.NANOSECONDS.sleep(remainingTillOneMinuteOfSilence);
        }
    }


    @Override
    public void onMessage(Message message) {
        try {
            System.out.println("Message received!");
            if (message instanceof ObjectMessage) {
                messageHandler.handleMessage(((ObjectMessage) message).getObject());
            }
            message.acknowledge();
            System.out.println("Acknowledged message " + message.getJMSMessageID());
            timeOfLastMessage = System.nanoTime();
        } catch (JMSException e) {
            System.err.println("Error processing message: " + e.getMessage());
            e.printStackTrace();
        }
    }
}