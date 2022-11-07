package com.example.microservice_scrap_rss;

import com.example.microservice_scrap_rss.cassandra.Article;
import com.example.microservice_scrap_rss.cassandra.ArticleRepo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import com.example.microservice_scrap_rss.rssfeedscraper.Answer;


import java.util.List;

@Component
public class KafkaListeners {
    @Autowired
    private ArticleRepo articleRepo;
    @KafkaListener(topics = "rss", groupId = "rss_group_id")
    void listener(String data) throws JsonProcessingException {
        System.out.println("Listener received "+data+ " . Has been consumed!!!");
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        List<Article> participantJsonList = mapper.readValue(data, new TypeReference<>(){});
        System.out.println("GOT :" + participantJsonList);
        for(var item : participantJsonList)
            articleRepo.insertArticle(item.id(),item.title(),item.description(),item.pubDate(),item.link());
    }
}
