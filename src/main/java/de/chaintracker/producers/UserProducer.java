package de.chaintracker.producers;


import de.chaintracker.entity.User;
import de.chaintracker.events.UserCreated;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserProducer {

    protected String topic = "users";

}

