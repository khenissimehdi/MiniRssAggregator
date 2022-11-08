package com.example.microservice_scrap_rss.cassandra;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.core.type.DataTypes;
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder;
import com.datastax.oss.driver.api.querybuilder.schema.CreateTableWithOptions;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.data.cassandra.core.query.Criteria;
import org.springframework.data.cassandra.core.query.Query;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

@Repository
public class FeedByArticleRepo {
    private final CqlSession session;
    private final CassandraOperations template;

    public FeedByArticleRepo(CqlSession session) {
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
        CreateTableWithOptions createTable = SchemaBuilder.createTable("articlebyuser").ifNotExists()
                .withPartitionKey("feedId", DataTypes.UUID)
                .withPartitionKey("articleId", DataTypes.UUID);
        executeStatement(createTable.build(), keyspace);
    }

    public FeedByArticle insertArticleToFeed(UUID feedId, UUID articleId) {
        var a= new FeedByArticle(feedId,articleId);
        template.insert(a);
        return a;
    }
    public FeedByArticle getArticleByFeedID(UUID feedId) {
        return template.selectOne(Query.query(Criteria.where("feedId").is(feedId)), FeedByArticle.class);
    }
}
