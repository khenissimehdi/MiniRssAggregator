package com.example.microservice_scrap_rss.cassandra.repository;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.core.type.DataTypes;
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder;
import com.datastax.oss.driver.api.querybuilder.schema.CreateTableWithOptions;
import com.example.microservice_scrap_rss.cassandra.entity.FeedByArticle;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.data.cassandra.core.query.Criteria;
import org.springframework.data.cassandra.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class FeedByArticleRepo {
    private final CqlSession session;
    private final CassandraOperations template;

    public FeedByArticleRepo(CqlSession session) {
        this.session = session;
        this.template = new CassandraTemplate(session);
    }

    private void executeStatement(SimpleStatement statement, String keyspace) {
        if (keyspace != null)
            statement.setKeyspace(CqlIdentifier.fromCql(keyspace));
        session.execute(statement);
    }

    public void createTable(String keyspace) {
        CreateTableWithOptions createTable = SchemaBuilder.createTable("feedbyarticle").ifNotExists()
                .withPartitionKey("feedId", DataTypes.UUID)
                .withClusteringColumn("articleId", DataTypes.UUID);
        executeStatement(createTable.build(), keyspace);
    }

    public void insertArticleToFeed(UUID feedId, UUID articleId) {
        var a = new FeedByArticle(feedId, articleId);
        template.insert(a);
    }

    public List<FeedByArticle> getArticleByFeedID(UUID feedId) {
        return template.select(Query.query(Criteria.where("feedid").is(feedId)), FeedByArticle.class);
    }
}