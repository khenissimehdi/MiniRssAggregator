package com.example.microservice_scrap_rss.cassandra;


import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.core.type.DataTypes;
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder;
import com.datastax.oss.driver.api.querybuilder.schema.CreateTable;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.data.cassandra.core.query.Columns;
import org.springframework.data.cassandra.core.query.Criteria;
import org.springframework.data.cassandra.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class FeedRepo {
    private final CqlSession session;
    private final CassandraOperations template;


    public FeedRepo(CqlSession session) {
        this.session = session;
        this.template = new CassandraTemplate(session);
    }

    private ResultSet executeStatement(SimpleStatement statement, String keyspace) {
        if (keyspace != null) {
            statement.setKeyspace(CqlIdentifier.fromCql(keyspace));
        }

        return session.execute(statement);
    }

    public void createTable(String keyspace) {
        CreateTable createTable = SchemaBuilder.createTable("feed").ifNotExists()
                .withPartitionKey("feedId", DataTypes.UUID)
                .withColumn("feedLink", DataTypes.TEXT);
        executeStatement(createTable.build(), keyspace);
    }

    public UUID insertFeed(String feedLink) {
        var id = UUID.randomUUID();
        var a  = new Feed(id,feedLink);
        template.insert(a);
        return id;
    }

    public void delete(UUID feedId) {
        template.delete(Query.query(Criteria.where("feedid").is(feedId)));
    }

}
