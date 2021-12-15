package com.example.consumer;

import static org.apache.kafka.clients.consumer.ConsumerConfig.AUTO_OFFSET_RESET_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.GROUP_ID_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.MAX_POLL_RECORDS_CONFIG;

import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;

@Configuration
@Slf4j
public class ConsumerConfig {
    public static final String TOPIC = "example-topic";

    @Bean
    ConcurrentMessageListenerContainer<String, String> container() {
        ContainerProperties containerProperties = new ContainerProperties(TOPIC);
        containerProperties.setMessageListener(new MyMessageListener());
        containerProperties.setIdleBetweenPolls(1000);

        ConcurrentMessageListenerContainer<String, String> container = new ConcurrentMessageListenerContainer<>(
                consumerFactory(), containerProperties);
        container.setErrorHandler(new MyListenerErrorHandler());

        return container;
    }

    @Bean
    ConsumerFactory<String, String> consumerFactory() {
        Map<String, Object> config = Map.of(
                BOOTSTRAP_SERVERS_CONFIG, "localhost:9092",
                GROUP_ID_CONFIG, "my-consumers",
                AUTO_OFFSET_RESET_CONFIG, "earliest",
                MAX_POLL_RECORDS_CONFIG, 2);

        return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(), new StringDeserializer());
    }
}
