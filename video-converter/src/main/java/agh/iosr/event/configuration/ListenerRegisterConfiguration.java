package agh.iosr.event.configuration;

import agh.iosr.event.listener.EventMessageListener;
import com.amazon.sqs.javamessaging.SQSConnection;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.annotation.PostConstruct;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;

@Configuration
@Import(EventQueueConfiguration.class)
@ComponentScan(basePackageClasses = {EventMessageListener.class})
@RequiredArgsConstructor
public class ListenerRegisterConfiguration {

    @Value("${aws.queue.name}")
    private String queueName;

    private final SQSConnection connection;

    private final EventMessageListener eventMessageListener;

    private Logger logger = LoggerFactory.getLogger(ListenerRegisterConfiguration.class);

    @PostConstruct
    private void registerListeners(){

        try {
            Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
            MessageConsumer consumer = session.createConsumer(session.createQueue(queueName));

            //register this callback
            consumer.setMessageListener(eventMessageListener);

            // start processing messages
            connection.start();

        } catch (JMSException e) {
            logger.error("Error creating connection to SQS");
            e.printStackTrace();
        }
    }
}
