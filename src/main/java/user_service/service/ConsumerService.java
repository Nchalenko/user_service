package user_service.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import user_service.model.Message;

@Service
public class ConsumerService {

    @KafkaListener(topics = "messages", groupId = "message_group_id")
    public void consume(Message message){
        System.out.println("Consuming the message: " + message);
    }
}