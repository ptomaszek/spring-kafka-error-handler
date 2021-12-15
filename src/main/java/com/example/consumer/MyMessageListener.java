package com.example.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.listener.MessageListener;

@Slf4j
public class MyMessageListener implements MessageListener<String, String> {

    @Override
    public void onMessage(ConsumerRecord<String, String> record) {
        log.info("Processing record at offset = {} with value = {} ...", record.offset(), record.value());

        if (record.value().contains("invalid")) {
            throw new IllegalArgumentException("Invalid data");
        }

        log.info("Processing record at offset = {} with value = {} ... OK", record.offset(), record.value());
    }
}
