package de.chaintracker.config;

import de.chaintracker.events.UserInfected;
import de.chaintracker.events.UserScanned;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

@Configuration
public class UserInfectedSenderConfig extends UserSenderConfig {

    @Bean
    public ProducerFactory<String, UserInfected> userInfectedProducerFactory() {
        return new DefaultKafkaProducerFactory<>(userSenderConfig());
    }


    @Bean
    public KafkaTemplate<String, UserInfected> userInfectedScannedKafkaTemplate() {
        return new KafkaTemplate<>(userInfectedProducerFactory());
    }


}
