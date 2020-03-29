package de.chaintracker.config;

import de.chaintracker.events.UserLocated;
import de.chaintracker.events.UserScanned;
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
public class UserLocatedSenderConfig extends UserSenderConfig {

    @Bean
    public ProducerFactory<String, UserLocated> userLocatedProducerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }


    @Bean
    public KafkaTemplate<String, UserLocated> userLocatedKafkaTemplate() {
        return new KafkaTemplate<>(userLocatedProducerFactory());
    }


}
