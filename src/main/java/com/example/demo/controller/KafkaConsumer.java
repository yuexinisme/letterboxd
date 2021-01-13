package com.example.demo.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.PartitionOffset;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaConsumer {

    @KafkaListener(groupId = "xx",
            topicPartitions = @TopicPartition(topic = "quickstart-events",
                    partitionOffsets = {
                            @PartitionOffset(partition = "0", initialOffset = "0")}))
    public String consume(ConsumerRecord message) {
        log.error(message.value().toString());
        //log.error(message.key().toString());
        return "xixi";
    }
}
