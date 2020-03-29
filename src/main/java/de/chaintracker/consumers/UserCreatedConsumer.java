package de.chaintracker.consumers;

import de.chaintracker.kafka.events.UserUpdated;
import de.chaintracker.service.MainService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
class UserUpdateConsumer {

    private static final Logger LOG = LoggerFactory.getLogger(UserUpdateConsumer.class);


    @KafkaListener(topics = "users",
            containerFactory = "userUpdatedKafkaListenerContainerFactory")
    public void userCreatedListener(UserUpdated userUpdated) {


        LOG.info("!!!user " + userUpdated.id + " updated!!!");

    }
}

