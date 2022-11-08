package com.example.microservice_scrap_rss.cassandra;

import com.example.microservice_scrap_rss.ProjectConstants;
import com.example.microservice_scrap_rss.rssfeedscraper.Answer;
import com.example.microservice_scrap_rss.rssfeedscraper.Engine;
import kotlin.collections.ArrayDeque;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.*;

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
        keyspaceRepository.createKeyspace(ProjectConstants.KEYSPACE.env(), 1);
        // Tables

    }
    private void createTables(){
        userRepo.createTable(ProjectConstants.KEYSPACE.env());
        articleRepo.createTable(ProjectConstants.KEYSPACE.env());
        feedByUserRepo.createTable(ProjectConstants.KEYSPACE.env());
        articleByUserRepo.createTable(ProjectConstants.KEYSPACE.env());
        feedRepo.createTable(ProjectConstants.KEYSPACE.env());
    }
    private void testInsert(){
//        var us=userRepo.insertUser();
        var us =UUID.fromString("d1e4f26a-8e6a-4e2b-9730-967a20bf85d7");
//        var a = articleRepo.insertArticle("title","desc");
//        articleByUserRepo.insertArticleToUser(us,a);
//        feedRepo.insertFeed("www.zebi.com");
    }

    @EventListener(ApplicationReadyEvent.class)
    public void runAfterStartup() throws InterruptedException {
        createAll();
        keyspaceRepository.useKeyspace(ProjectConstants.KEYSPACE.env());
        createTables();
        //testInsert();
        //articleByUserRepo.insertArticleToUser(UUID.fromString("dc0578c3-c418-4953-87c8-82d2b32e77a9"),UUID.randomUUID());
        //articleByUserRepo.insertArticleToUser(UUID.fromString("dc0578c3-c418-4953-87c8-82d2b32e77a9"),UUID.randomUUID());
        //articleByUserRepo.insertArticleToUser(UUID.fromString("dc0578c3-c418-4953-87c8-82d2b32e77a9"),UUID.randomUUID());


        var feeds = feedByUserRepo.getAllFeedsOf(UUID.fromString("dc0578c3-c418-4953-87c8-82d2b32e77a9"));
        //var c = articleByUserRepo.getLast10ArticlesOf(us);;


        feeds.forEach(System.out::println);

        feedByUserRepo.removeFeedFromUser(UUID.fromString("dc0578c3-c418-4953-87c8-82d2b32e77a9"), UUID.fromString("265acc9c-95b5-44e6-950d-5ee5e25f1ba3"));
        List<Answer> list= new ArrayList<>();
        list.add(new Answer(UUID.randomUUID(),"Lord zebi the Rings","Book about adventures", LocalDate.now(),"google.fr"));

        feedRepo.insertFeed("https://www.lemonde.fr/rss/une.xml");
        var listofeed = feedRepo.getAllFeeds().stream().map(Feed::getFeedLink).toList();



        RestTemplate restTemplate= new RestTemplate();
        var aggregator = Engine.createEngineFromList(listofeed,400,150);
        var answer = aggregator.retrieve();

        var arr = answer.stream().map(Optional::get).toList();
        var response= restTemplate.postForObject(
                "http://localhost:8080/api/v1/articles/save",
        arr, Answer[].class);

    }


}