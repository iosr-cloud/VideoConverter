package agh.iosr.event.impl;

import com.amazon.sqs.javamessaging.SQSConnection;
import com.amazon.sqs.javamessaging.SQSConnectionFactory;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;

@Service
public class MessageReceiver {

//    @Autowired
//    EventQueueConfiguration config;

    @Value("${aws.queue.access_key_id}")
    private String awsId;

    @Value("${aws.queue.secret}")
    private String awsKey;

    @Value("${aws.region}")
    private String region;

    @Value("${aws.queue.name}")
    private String queueName;

    SQSConnection connection;

    @PostConstruct
    private void init() throws JMSException, InterruptedException {
        // Create the connection factory based on the config
        BasicAWSCredentials awsCreds = new BasicAWSCredentials(awsId, awsKey);

        SQSConnectionFactory connectionFactory = new SQSConnectionFactory.Builder()
                .withRegionName(region)
                .withAWSCredentialsProvider(new AWSStaticCredentialsProvider(awsCreds))
                .build();

        // Create the connection
        connection = connectionFactory.createConnection();
        // Create the session
        Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
        MessageConsumer consumer = session.createConsumer(session.createQueue(queueName));
        ReceiverCallback callback = new ReceiverCallback();

        consumer.setMessageListener(callback);

        // No messages are processed until this is called
        connection.start();

//        callback.waitForOneMinuteOfSilence();
//        System.out.println( "Returning after one minute of silence" );


    }

    @PreDestroy
    private void destroy() throws JMSException {
        // Close the connection. This closes the session automatically
        connection.close();
        System.out.println( "Connection closed" );
    }


}
