package com.example.microservice_scrap_rss.cassandra;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.CqlSessionBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetSocketAddress;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Configuration
public class CassandraConnector {

    private CqlSession session;

    @Bean
    public CqlSession connect() {
        CqlSessionBuilder builder = CqlSession.builder();
        builder.addContactPoint(new InetSocketAddress("localhost", 9042));
        builder.withLocalDatacenter("datacenter1");
        session = builder.build();



        return session;
    }

    public CqlSession getSession() {
        return this.session;
    }

    public void close() {
        session.close();
    }

}