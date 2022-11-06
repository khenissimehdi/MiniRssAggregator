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

    @EventListener(ApplicationReadyEvent.class)
    public void runAfterStartup() {
        keyspaceRepository.createKeyspace("test", 1);
        keyspaceRepository.useKeyspace("test");
        userRepo.createTable("user");
        articleRepo.createTable("test");
        userRepo.insertUser();
        var article=articleRepo.insertArticle("test","desc test");
//        userRepo.subscribe(UUID.fromString("dc0578c3-c418-4953-87c8-82d2b32e77a9"),article);
    }


}