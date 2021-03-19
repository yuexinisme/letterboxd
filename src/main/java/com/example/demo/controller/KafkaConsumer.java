package com.example.demo.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaConsumer {

//    @KafkaListener(groupId = "xx",
//            topicPartitions = @TopicPartition(topic = "quickstart-events",
//                    partitionOffsets = {
//                            @PartitionOffset(partition = "0", initialOffset = "0")}))
//    public String consume(ConsumerRecord message) {
//        log.error(message.value().toString());
//        //log.error(message.key().toString());
//        return "xixi";
//    }
}
