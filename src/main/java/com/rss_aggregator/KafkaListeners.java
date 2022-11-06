package com.rss_aggregator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import rssfeedscraper.Answer;

import java.util.List;

@Component
public class KafkaListeners {
    @KafkaListener(topics = "rss", groupId = "rss_group_id")
    void listener(String data) throws JsonProcessingException {
        System.out.println("Listener received "+data+ " . Has been consumed!!!");
        ObjectMapper mapper = new ObjectMapper();
        List<Answer> participantJsonList = mapper.readValue(data, new TypeReference<List<Answer>>(){});
        System.out.println("GOT :" + participantJsonList);
    }


}
