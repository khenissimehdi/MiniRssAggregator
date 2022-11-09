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
    private FeedByArticleRepo feedByArticleRepo;

    @Autowired
    private FeedByUserRepo feedByUserRepo;
    @KafkaListener(topics = "rss", groupId = "rss_group_id")
    void listener(String data) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        List<Article> participantJsonList = mapper.readValue(data, new TypeReference<>(){});

        var users = userRepo.getAllUsers();
        users.forEach(user -> {
            var feedsOfUser = feedByUserRepo.getAllFeedsOf(user.getId());
            feedsOfUser.forEach(feed -> {
               var articlesByFeed = feedByArticleRepo.getArticleByFeedID(feed.getFeedId());
               articlesByFeed.forEach(articles -> {
                   articleByUserRepo.insertArticleToUser(user.getId(),articles.getArticleId());
               });
            });
        });

        // Insert Articles
        for(var item : participantJsonList) {
            articleRepo.insertArticle(item.id(),item.title(),item.description(), item.pubDate(),item.link());
        }

    }
}
