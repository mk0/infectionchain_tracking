package de.chaintracker.config;

import de.chaintracker.entity.User;
import de.chaintracker.events.UserCreated;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
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
public class UserCreatedSenderConfig  extends UserSenderConfig {

    @Bean
    public ProducerFactory<String, UserCreated> userCreatedProducerFactory() {
        return new DefaultKafkaProducerFactory<>(userSenderConfig());
    }

    @Bean
    public KafkaTemplate<String, UserCreated> userCreateKafkaTemplate() {
        return new KafkaTemplate<>(userCreatedProducerFactory());
    }


}
