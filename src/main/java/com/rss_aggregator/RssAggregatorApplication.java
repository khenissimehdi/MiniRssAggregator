package com.rss_aggregator;

import com.rss_aggregator.cassandra.CassandraConnector;

import com.rss_aggregator.cassandra.KeyspaceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;

@SpringBootApplication
public class RssAggregatorApplication {

    private static final Logger LOG = LoggerFactory.getLogger(RssAggregatorApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(RssAggregatorApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(KafkaTemplate<String, String> kafkaTemplate) {
        return args -> {
            kafkaTemplate.send("rss", "hello kafka");
        };
    }

    @Bean
    CommandLineRunner commandLineRunner() {
        return args -> {
            var connector = new CassandraConnector();
            var session = connector.connect();
            KeyspaceRepository keyspaceRepository = new KeyspaceRepository(session);
            keyspaceRepository.createKeyspace("neskey",2);
        };
    }
}
