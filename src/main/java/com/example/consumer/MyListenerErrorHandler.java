package com.example.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.TopicPartition;
import org.springframework.kafka.listener.ContainerAwareErrorHandler;
import org.springframework.kafka.listener.MessageListenerContainer;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
class MyListenerErrorHandler implements ContainerAwareErrorHandler {

    @Override
    public void handle(Exception thrownException,
                       List<ConsumerRecord<?, ?>> records,
                       Consumer<?, ?> consumer,
                       MessageListenerContainer container) {
        ConsumerRecord<?, ?> record = records.get(0);

        log.warn("Handling failure when processing record at offset = {} with value = {}. Offsets in poll = [{}] ...",
                record.offset(), record.value(),
                records.stream().map(ConsumerRecord::offset).map(String::valueOf).collect(Collectors.joining(", ")));

        if (record.value().equals("invalid-fatal-recovery")) {
            simulateBugInErrorHandling(record);
        }

        skipFailedRecord(consumer, record);
        log.warn("Handling failure when processing record at offset = {} ... SKIPPED", record.offset());
    }

    private void skipFailedRecord(Consumer<?, ?> consumer, ConsumerRecord<?, ?> record) {
        consumer.seek(new TopicPartition(record.topic(), record.partition()), record.offset() + 1);
    }

    private void simulateBugInErrorHandling(ConsumerRecord<?, ?> record) {
        throw new NullPointerException(
                "DB transaction failed when saving info about failure on offset = " + record.offset());
    }
}
