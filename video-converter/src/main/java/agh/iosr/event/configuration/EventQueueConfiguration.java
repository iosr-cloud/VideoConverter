package agh.iosr.event.configuration;

import com.amazon.sqs.javamessaging.SQSConnection;
import com.amazon.sqs.javamessaging.SQSConnectionFactory;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.jms.JMSException;

@Configuration
public class EventQueueConfiguration {

    @Value("${aws.queue.access_key_id}")
    private String awsId;

    @Value("${aws.queue.secret}")
    private String awsKey;

    @Value("${aws.region}")
    private String region;

    @Bean
    public SQSConnectionFactory sqsConnectionFactory(){
        BasicAWSCredentials awsCreds = new BasicAWSCredentials(awsId, awsKey);
        return new SQSConnectionFactory.Builder()
                .withRegionName(region)
                .withAWSCredentialsProvider(new AWSStaticCredentialsProvider(awsCreds))
                .build();
    }

    @Bean(destroyMethod = "close")
    public SQSConnection sqsConnection() throws JMSException {
        return sqsConnectionFactory().createConnection();
    }

}
