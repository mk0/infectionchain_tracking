package de.chaintracker.consumers;

import de.chaintracker.events.UserCreated;
import de.chaintracker.service.MainService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@KafkaListener(topics = "users",
        containerFactory = "userCreatedKafkaListenerContainerFactory")
class UserCreatedConsumer {

    private static final Logger LOG = LoggerFactory.getLogger(MainService.class);



    public void userCreatedListener(UserCreated userCreated) {

        LOG.info("!!!user created!!!");

    }
}

