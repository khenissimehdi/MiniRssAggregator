package com.rss_aggregator.cassandra;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.CqlSessionBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetSocketAddress;

@Configuration
public class CassandraConnector {

    private CqlSession session;

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