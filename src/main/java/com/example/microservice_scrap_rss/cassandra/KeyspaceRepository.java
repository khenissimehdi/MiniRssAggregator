package com.example.microservice_scrap_rss.cassandra;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.servererrors.InvalidQueryException;
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder;
import com.datastax.oss.driver.api.querybuilder.schema.CreateKeyspace;
import com.example.microservice_scrap_rss.ProjectConstants;
import org.springframework.stereotype.Repository;

@Repository
public class KeyspaceRepository  {
    public CqlSession session;

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

    public boolean checkIfKeySpaceIsUsed() {
          try {
              session.execute("DESC " + CqlIdentifier.fromCql(ProjectConstants.KEYSPACE.env()) ) ;
              return true;
          } catch (InvalidQueryException e) {
              return false;
          }

    }
}