package com.example.microservice_scrap_rss;

import com.example.microservice_scrap_rss.cassandra.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import com.example.microservice_scrap_rss.rssfeedscraper.Answer;


import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Component
public class KafkaListeners {
    @Autowired
    private ArticleRepo articleRepo;

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private ArticleByUserRepo articleByUserRepo;

    @Autowired
    private FeedRepo feedRepo;

    @Autowired
    private FeedByUserRepo feedByUserRepo;
    @KafkaListener(topics = "rss", groupId = "rss_group_id")
    void listener(String data) throws JsonProcessingException {
        System.out.println("Listener received "+data+ " . Has been consumed!!!");
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        List<Article> participantJsonList = mapper.readValue(data, new TypeReference<>(){});

        var users = userRepo.getAllUsers();
        //var ) = feedByUserRepo.getAllFeedsOf(UUID.randomUUID());


        for(var item : participantJsonList) {
            articleByUserRepo.insertArticleToUser(UUID.fromString("dc0578c3-c418-4953-87c8-82d2b32e77a9"),item.id());
            articleRepo.insertArticle(item.id(),item.title(),item.description(), item.pubDate(),item.link());
        }

    }
}
