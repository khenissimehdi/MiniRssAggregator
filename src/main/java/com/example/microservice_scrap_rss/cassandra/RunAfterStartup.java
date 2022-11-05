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

    @EventListener(ApplicationReadyEvent.class)
    public void runAfterStartup() {
//        keyspaceRepository.createKeyspace("test", 1);
        keyspaceRepository.useKeyspace("test");
//        userRepo.createTable("user");
//        userRepo.insertUser();
        userRepo.subscribe(UUID.fromString("ad791599-e0a4-4520-a522-656212b56dcd"),UUID.randomUUID());



    }


}