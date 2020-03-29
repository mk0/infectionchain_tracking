package de.chaintracker.config;

import de.chaintracker.events.UserCreated;
import de.chaintracker.events.UserGotScanned;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class UserGotScannedSenderConfig  extends UserSenderConfig {
    @Bean
    public ProducerFactory<String, UserGotScanned> userGotScannedProducerFactory() {
        return new DefaultKafkaProducerFactory<>(userSenderConfig());
    }


    @Bean
    public KafkaTemplate<String, UserGotScanned> userGotScannedKafkaTemplate() {
        return new KafkaTemplate<>(userGotScannedProducerFactory());
    }


}
