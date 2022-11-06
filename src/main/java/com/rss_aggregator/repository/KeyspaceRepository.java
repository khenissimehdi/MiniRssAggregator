package com.rss_aggregator.repository;

import org.springframework.stereotype.Repository;

@Repository
public class KeyspaceRepository  {
//    public  CqlSession session;
//
//    public KeyspaceRepository(CqlSession session) {
//        this.session = session;
//    }
//
//    public void createKeyspace(String keyspaceName, int numberOfReplicas) {
//        CreateKeyspace createKeyspace = SchemaBuilder.createKeyspace(keyspaceName)
//          .ifNotExists()
//          .withSimpleStrategy(numberOfReplicas);
//        session.execute(createKeyspace.build());
//    }
//
//    public void useKeyspace(String keyspace) {
//        session.execute("USE " + CqlIdentifier.fromCql(keyspace));
//    }
}