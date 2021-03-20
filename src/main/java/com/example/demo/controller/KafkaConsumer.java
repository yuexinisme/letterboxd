package com.example.demo.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.KafkaListeners;
import org.springframework.kafka.annotation.PartitionOffset;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaConsumer {

    @KafkaListener(groupId = "3", topics = "kaka")
    public String consume(ConsumerRecord message, Acknowledgment ack) {
        log.error(message.value().toString());
        //log.error(message.key().toString());
        long os = message.offset();
        System.out.println("received~~ " + os);
        System.out.println(message.value().toString());
        ack.acknowledge();
        return "xixi";
    }
}
