package de.chaintracker.producers;


import de.chaintracker.entity.ContactEvent;
import de.chaintracker.events.UserGotScanned;
import de.chaintracker.events.UserScanned;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserGotScannedProducer extends UserProducer {

    private KafkaTemplate<String, UserGotScanned> userProducer;

    public UserGotScannedProducer(KafkaTemplate<String, UserGotScanned> userProducer) {
        this.userProducer = userProducer;
    }
    public void send(ContactEvent contactEvent) {

        var event = new UserGotScanned();
        event.userKey = contactEvent.getUser2().getId();
        event.location = "POINT (" + Math.toDegrees(contactEvent.getLocationEvent().getLongitude()) +" " + Math.toDegrees(contactEvent.getLocationEvent().getLatitude())+")";
        event.scanningUserKey = contactEvent.getUser1().getId();
        userProducer.send(topic, event);
    }
}

