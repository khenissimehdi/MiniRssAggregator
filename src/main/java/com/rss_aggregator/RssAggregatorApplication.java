package com.rss_aggregator;


import com.rss_aggregator.cassandra.CassandraConnector;
import com.rss_aggregator.entity.Article;
import com.rss_aggregator.repository.KeyspaceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.cassandra.CassandraDataAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;
import rssfeedscraper.Answer;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication(exclude = {
        CassandraDataAutoConfiguration.class
})
public class RssAggregatorApplication {

    private static final Logger LOG = LoggerFactory.getLogger(RssAggregatorApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(RssAggregatorApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(KafkaTemplate<String, List<Answer>> kafkaTemplate) {
        var answer = new Answer("ta","mere","lapute");
        return args -> kafkaTemplate.send("rss", List.of(answer));
    }

//    @Bean
//    CommandLineRunner commandLineRunner() {
//        return args -> {
//            var connector = new CassandraConnector();
//            var session = connector.connect();
//            KeyspaceRepository keyspaceRepository = new KeyspaceRepository(session);
//            keyspaceRepository.createKeyspace("neskey",2);
//        };
//    }
}
