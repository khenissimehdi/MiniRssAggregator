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

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Repository
public class FeedByUserRepo {
    private final CqlSession session;
    private final CassandraOperations template;


    public FeedByUserRepo(CqlSession session) {
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
        CreateTable createTable = SchemaBuilder.createTable("feedbyuser").ifNotExists()
                .withPartitionKey("userId", DataTypes.UUID)
                .withClusteringColumn("feedId", DataTypes.UUID);
        executeStatement(createTable.build(), keyspace);
    }



    public FeedByUser insertFeedToUser(UUID userId, UUID feedId) {
        var a= new FeedByUser(userId,feedId);
        template.insert(a);
        return a;
    }

    public List<ArtcileByUser> getAllFeedsOf(UUID userId) {
        // To do sorting
        return template.select(Query.query(Criteria.where("userid").is(userId)).columns(Columns.from("articleid")).limit(10), ArtcileByUser.class);
    }
}
