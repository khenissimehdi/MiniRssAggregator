package com.example.microservice_scrap_rss;

import com.datastax.oss.driver.api.core.CqlSession;
import com.example.microservice_scrap_rss.cassandra.CassandraConnector;
import com.example.microservice_scrap_rss.cassandra.KeyspaceRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@SpringBootApplication
public class MicroserviceScrapRssApplication {


    private static final Logger LOG = LoggerFactory.getLogger(MicroserviceScrapRssApplication.class);

    public static void main(String[] args) {

        SpringApplication.run(MicroserviceScrapRssApplication.class, args);
    }

//    @Bean
//    CommandLineRunner commandLineRunner(KafkaTemplate<String, String> kafkaTemplate) {
//        return args -> {
//            kafkaTemplate.send("rss", "hello kafka");
//        };
//    }


    @Bean
    CommandLineRunner commandLineRunner() {
        return args -> {
            var connector = new CassandraConnector();
           // var session = connector.connect();
         //   KeyspaceRepository keyspaceRepository = new KeyspaceRepository(session);
            //keyspaceRepository.createKeyspace("neskey",2);

        };
    }
}
