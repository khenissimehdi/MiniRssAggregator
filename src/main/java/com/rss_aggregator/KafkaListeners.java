package com.rss_aggregator;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaListeners {
    @KafkaListener(topics = "rss", groupId = "rss_group_id")
    void listener(String data){
        System.out.println("Listener received "+data+ "it has been consumed!!!");
    }
}
