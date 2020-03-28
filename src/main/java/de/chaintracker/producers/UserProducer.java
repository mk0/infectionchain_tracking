package de.chaintracker.producers;


import de.chaintracker.entity.User;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserProducer {
    private KafkaTemplate<String, User> userProducer;

    public UserProducer(KafkaTemplate<String, User> userProducer) {
        this.userProducer = userProducer;
    }
    public void send(User user) {
        userProducer.send("test-topic", user);
    }
}

