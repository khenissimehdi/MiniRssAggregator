package com.example.microservice_scrap_rss.cassandra;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

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
    private ArticleByUserRepo articleByUserRepo;

    @Autowired
    private FeedRepo feedRepo;

    private void createAll(){
        // Keyspace
        keyspaceRepository.createKeyspace("test", 1);
        // Tables
        createTables();
    }
    private void createTables(){
        userRepo.createTable("test");
        articleRepo.createTable("test");
        feedByUserRepo.createTable("test");
        articleByUserRepo.createTable("test");
        feedRepo.createTable("test");
    }
    private void testInsert(){
//        var us=userRepo.insertUser();
        var us =UUID.fromString("d1e4f26a-8e6a-4e2b-9730-967a20bf85d7");
//        var a = articleRepo.insertArticle("title","desc");
//        articleByUserRepo.insertArticleToUser(us,a);
//        feedRepo.insertFeed("www.zebi.com");
//        feedByUserRepo.insertFeedToUser(UUID.fromString("dc0578c3-c418-4953-87c8-82d2b32e77a9"), UUID.randomUUID());
        var c = articleByUserRepo.getLast10ArticlesOf(us);
        c.forEach(System.out::println);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void runAfterStartup() {
        keyspaceRepository.useKeyspace("test");
//        createAll();
        testInsert();




    }


}