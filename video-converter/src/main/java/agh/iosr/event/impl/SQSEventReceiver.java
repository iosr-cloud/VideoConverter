package agh.iosr.event.impl;

import agh.iosr.event.api.EventReceiver;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class SQSEventReceiver implements EventReceiver {

    private Logger logger = LoggerFactory.getLogger(SQSEventReceiver.class);

    @Value("${aws.queue.name}")
    private String queueName;

    private AmazonSQS amazonSQS;
    private String queueUrl;

    public SQSEventReceiver(AmazonSQS amazonSQS) {
        this.amazonSQS = amazonSQS;
    }

    @PostConstruct
    private void init(){
        this.queueUrl = amazonSQS.getQueueUrl(queueName).getQueueUrl();
    }

    public List<Message> receiveEventMessages() {

        ReceiveMessageRequest messageRequest = new ReceiveMessageRequest(queueUrl);
        return amazonSQS
                .receiveMessage(messageRequest)
                .getMessages();
    }

    @Override
    public void deleteEventMessages(List<Message> messages) {
        messages.stream()
                .map(Message::getReceiptHandle)
                .map(receiptHandle -> new DeleteMessageRequest(queueUrl, receiptHandle))
                .forEach(deleteMessage -> amazonSQS.deleteMessage(deleteMessage));
    }


}
