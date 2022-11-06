package com.rss_aggregator.cassandra;

import com.rss_aggregator.repository.KeyspaceRepository;
import com.rss_aggregator.repository.PersonRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class RunAfterStartup {



//    public RunAfterStartup(KeyspaceRepository keyspaceRepository, PersonRepository personRepository) {
//        Objects.requireNonNull(keyspaceRepository);
//        Objects.requireNonNull(personRepository);
//        this.keyspaceRepository = keyspaceRepository;
//        this.personRepository = personRepository;
//    }
//
//    @EventListener(ApplicationReadyEvent.class)
//    public void runAfterStartup() {
//        //  keyspaceRepository.createKeyspace("test", 1);
//       // keyspaceRepository.useKeyspace("test");
//        //  cassandraTemplateRepo.createTable("test");
//        // personRepo.insertPerson( "mehdi",50);
//       // var person = personRepo.getPersonById(UUID.fromString("2bbfb169-d945-4ee0-9520-71e81cc6f3c"));
//        //System.out.println(person);
//    }
}