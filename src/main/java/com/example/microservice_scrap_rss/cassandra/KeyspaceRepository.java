package com.example.microservice_scrap_rss.cassandra;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder;
import com.datastax.oss.driver.api.querybuilder.schema.CreateKeyspace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Repository
public class KeyspaceRepository  {
    public final CqlSession session;

    public KeyspaceRepository(CqlSession session) {
        this.session = session;
    }

    public void createKeyspace(String keyspaceName, int numberOfReplicas) {
        CreateKeyspace createKeyspace = SchemaBuilder.createKeyspace(keyspaceName)
          .ifNotExists()
          .withSimpleStrategy(numberOfReplicas);
        session.execute(createKeyspace.build());
    }

    public void useKeyspace(String keyspace) {
        session.execute("USE " + CqlIdentifier.fromCql(keyspace));
    }
}