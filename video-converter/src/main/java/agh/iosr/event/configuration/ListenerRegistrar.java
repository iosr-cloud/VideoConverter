package agh.iosr.event.configuration;

import agh.iosr.event.listener.EventMessageListener;
import com.amazon.sqs.javamessaging.SQSConnection;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;

@Component
@RequiredArgsConstructor
public class ListenerRegistrar implements CommandLineRunner {

    @Value("${aws.queue.name}")
    private String queueName;

    private final SQSConnection connection;

    private final EventMessageListener eventMessageListener;

    private Logger logger = LoggerFactory.getLogger(ListenerRegistrar.class);
    private Session session;
    private MessageConsumer consumer;

    @Override
    public void run(String... args) throws Exception {
        try {
            session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
            consumer = session.createConsumer(session.createQueue(queueName));

            //register this callback
            consumer.setMessageListener(eventMessageListener);

            // start processing messages
            connection.start();

        } catch (JMSException e) {
            logger.error("Error creating connection to SQS");
            e.printStackTrace();
        }
    }

    @PreDestroy
    private void destroy(){
        try {
            consumer.close();
            session.close();
            connection.close();
        } catch (JMSException e) {
            logger.error("Couldn't close SQS connection");
            e.printStackTrace();
        }
    }
}
