package de.chaintracker.config;

import de.chaintracker.events.UserCreated;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

@Configuration
@EnableKafka
public class UserCreatedConsumerConfig extends UserConsumerConfig {

    @Bean
    @ConditionalOnMissingBean(ConsumerFactory.class)
    public ConsumerFactory<String, UserCreated> userCreatedConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(
                userConsumerConfig(),
                new StringDeserializer(),
                new JsonDeserializer<>(UserCreated.class));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, UserCreated>
    userCreatedKafkaListenerContainerFactory() {

        ConcurrentKafkaListenerContainerFactory<String, UserCreated> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(userCreatedConsumerFactory());
        return factory;
    }
}
