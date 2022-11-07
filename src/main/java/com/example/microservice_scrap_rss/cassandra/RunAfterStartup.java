package com.example.microservice_scrap_rss.cassandra;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

@Component
public class RunAfterStartup {

    @Autowired
    private KeyspaceRepository keyspaceRepository;

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private ArticleRepo articleRepo;

    @Autowired
    private FeedByUserRepo feedByUserRepo;

    @Autowired
    private ArticleByUserRepo articlebRepo;

    @Autowired
    private FeedRepo feedRepo;

    @EventListener(ApplicationReadyEvent.class)
    public void runAfterStartup() {
      // keyspaceRepository.createKeyspace("test", 1);
       keyspaceRepository.useKeyspace("test");
//     userRepo.createTable("user");
        //articlebRepo.createTable("test");
       // articlebRepo.insertArticle();
       // feedByUserRepo.createTable("test");
        articlebRepo.createTable("test");
        articlebRepo.insertArticleToUser(UUID.randomUUID(),UUID.randomUUID());
        feedRepo.createTable("test");
        feedRepo.insertFeed("www.zebi.com");
       // feedByUserRepo.insertFeedToUser(UUID.fromString("dc0578c3-c418-4953-87c8-82d2b32e77a9"),UUID.randomUUID());
        //var c = articlebRepo.getLast10ArticlesOf(UUID.fromString("dd158d72-2341-4f70-b01a-dac85930cd61"));
       // System.out.println(c.get(0).articleId);
//        userRepo.insertUser();
//        var article=articleRepo.insertArticle("test","desc test",".");
//        userRepo.subscribe(UUID.fromString("dc0578c3-c418-4953-87c8-82d2b32e77a9"),article);
    }


}