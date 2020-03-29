package de.chaintracker.producers;


import de.chaintracker.entity.ContactEvent;
import de.chaintracker.entity.LocationEvent;
import de.chaintracker.events.UserLocated;
import de.chaintracker.events.UserScanned;
import de.chaintracker.util.Geocoord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserScannedProducer extends UserProducer {

    private KafkaTemplate<String, UserScanned> userProducer;

    public UserScannedProducer(KafkaTemplate<String, UserScanned> userProducer) {
        this.userProducer = userProducer;
    }

    public void send(ContactEvent contactEvent) {

        var event = new UserScanned();
        event.userKey = contactEvent.getUser1().getId();
        event.location = "POINT (" + contactEvent.getLocationEvent().getLongitude() + " " + contactEvent.getLocationEvent().getLatitude() + ")";
        event.scannedUserKey = contactEvent.getUser2().getId();
        userProducer.send(topic, event);
    }
}

