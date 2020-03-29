package de.chaintracker.producers;


import de.chaintracker.entity.User;
import de.chaintracker.events.UserCreated;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserCreatedProducer extends UserProducer {

    private KafkaTemplate<String, UserCreated> userProducer;

    public UserCreatedProducer(KafkaTemplate<String, UserCreated> userProducer) {
        this.userProducer = userProducer;
    }
    public void send(User user) {

        var event = new UserCreated();
        event.email = user.getEmail();
        event.firstName = user.getFirstName();
        event.lastName = user.getLastName();
        event.qrCode = user.getQrCode();
        event.userName = user.getUserName();
        event.userKey = user.getId();
        userProducer.send(topic, event);
    }
}

