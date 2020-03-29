package de.chaintracker.producers;


import de.chaintracker.entity.LocationEvent;
import de.chaintracker.entity.User;
import de.chaintracker.events.UserCreated;
import de.chaintracker.events.UserLocated;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserLocatedProducer extends UserProducer {

    private KafkaTemplate<String, UserLocated> userProducer;

    public UserLocatedProducer(KafkaTemplate<String, UserLocated> userProducer) {
        this.userProducer = userProducer;
    }
    public void send(LocationEvent locationEvent) {

        var event = new UserLocated();
        event.userKey = locationEvent.getUserCreate().getId();
        event.location = "POINT (" + Math.toDegrees(locationEvent.getLongitude()) +" " + Math.toDegrees(locationEvent.getLatitude())+")";
        userProducer.send(topic, event);
    }
}

