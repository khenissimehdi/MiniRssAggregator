package com.example.microservice_scrap_rss.cassandra;

import com.example.microservice_scrap_rss.ProjectConstants;
import com.example.microservice_scrap_rss.rssfeedscraper.Answer;
import com.example.microservice_scrap_rss.rssfeedscraper.Engine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

    @Autowired
    private FeedByArticleRepo feedByArticleRepo;

    private final ArrayList<UUID> feedUUIDS = new ArrayList<>();
    private final ArrayList<UUID>  usersUUIDS = new ArrayList<>();

    private void addFeeds() {
        var feed1 = feedRepo.insertFeed("https://www.lemonde.fr/rss/une.xml");
        var feed2 = feedRepo.insertFeed("https://feeds.fireside.fm/bibleinayear/rss");
        feedUUIDS.add(feed1);
        feedUUIDS.add(feed2);
    }



    private void addUsers() {
        IntStream.range(0,3).forEach(i ->  usersUUIDS.add(userRepo.insertUser()));
    }
    private void createTables(){
        userRepo.createTable(ProjectConstants.KEYSPACE.env());
        articleRepo.createTable(ProjectConstants.KEYSPACE.env());
        feedByUserRepo.createTable(ProjectConstants.KEYSPACE.env());
        articleByUserRepo.createTable(ProjectConstants.KEYSPACE.env());
        feedRepo.createTable(ProjectConstants.KEYSPACE.env());
        feedByArticleRepo.createTable(ProjectConstants.KEYSPACE.env());
    }


    private void subUsersToRandomFeeds() {
       usersUUIDS.forEach( uuid -> {
           Random rand = new Random();
           var randomUUID = feedUUIDS.get(rand.nextInt(feedUUIDS.size()));
           feedByUserRepo.insertFeedToUser(uuid,randomUUID);
       });
    }

    @EventListener(ApplicationReadyEvent.class)
    public void runAfterStartup() {
        if(!keyspaceRepository.checkIfKeySpaceIsUsed()) {
            initCassandra();
            scrapperSum();
        } else {
            cassandraAlreadySet();
        }
    }

    private void initCassandra() {
        keyspaceRepository.createKeyspace(ProjectConstants.KEYSPACE.env(), 1);
        keyspaceRepository.useKeyspace(ProjectConstants.KEYSPACE.env());
        createTables();
        addFeeds();
        addUsers();
        subUsersToRandomFeeds();
    }

    private void cassandraAlreadySet() {
        keyspaceRepository.useKeyspace(ProjectConstants.KEYSPACE.env());
        scrapperSum();
    }

    private void scrapperSum() {
        ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
        exec.scheduleAtFixedRate(() -> {
            try {
                System.out.println("GOING TO LAUNCH A SCRAP");
                var lisToFeed = feedRepo.getAllFeeds();
                RestTemplate restTemplate= new RestTemplate();
                var aggregator = Engine.createEngineFromList(lisToFeed,feedByArticleRepo,4000,400);
                List<Optional<Answer>> answer = aggregator.retrieve();
                var arr = answer.stream().flatMap(Optional::stream).collect(Collectors.toList());

                restTemplate.postForObject(
                        ProjectConstants.API_URL_DEV.env(),
                        arr, Answer[].class);

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }, 0, 1, TimeUnit.MINUTES);
    }


}