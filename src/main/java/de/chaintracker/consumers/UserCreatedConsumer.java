package de.chaintracker.consumers;

import de.chaintracker.events.UserCreated;
import de.chaintracker.service.MainService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
class UserCreatedConsumer {

    private static final Logger LOG = LoggerFactory.getLogger(MainService.class);


    @KafkaListener(topics = "users",
            containerFactory = "userCreatedKafkaListenerContainerFactory")
    public void userCreatedListener(UserCreated userCreated) {

        LOG.info("!!!user created!!!");

    }
}

