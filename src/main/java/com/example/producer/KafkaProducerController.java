package com.example.producer;

import com.example.consumer.ConsumerConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class KafkaProducerController {
    private final KafkaTemplate<String, String> kafkaTemplate;

    @GetMapping("/api/{val}")
    @ResponseBody
    public String reload(@PathVariable String val) {
        kafkaTemplate.send(ConsumerConfig.TOPIC, val); // set "invalid-recoverable/unrecoverable" to trigger a failure
        kafkaTemplate.send(ConsumerConfig.TOPIC, "alright!");
        kafkaTemplate.send(ConsumerConfig.TOPIC, "alright again!");

        return "OK";
    }
}
