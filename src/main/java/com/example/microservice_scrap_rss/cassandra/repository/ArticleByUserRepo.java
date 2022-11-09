package com.example.microservice_scrap_rss.cassandra.repository;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.core.metadata.schema.ClusteringOrder;
import com.datastax.oss.driver.api.core.type.DataTypes;
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder;
import com.datastax.oss.driver.api.querybuilder.schema.CreateTableWithOptions;
import com.example.microservice_scrap_rss.ProjectConstants;
import com.example.microservice_scrap_rss.cassandra.entity.ArticleByUser;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.data.cassandra.core.query.Criteria;
import org.springframework.data.cassandra.core.query.Query;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Repository
public class ArticleByUserRepo {
    private final CqlSession session;
    private final CassandraOperations template;

    public ArticleByUserRepo(CqlSession session) {
        Objects.requireNonNull(session);
        this.session = session;
        this.template = new CassandraTemplate(session);
    }

    private void executeStatement(SimpleStatement statement, String keyspace) {
        if (keyspace != null)
            statement.setKeyspace(CqlIdentifier.fromCql(keyspace));
        session.execute(statement);
    }

    public void createTable(String keyspace) {
        CreateTableWithOptions createTable = SchemaBuilder.createTable(ProjectConstants.TABLE_ARTICLE_BY_USER.env()).ifNotExists()
                .withPartitionKey("userId", DataTypes.UUID)
                .withColumn("articleId", DataTypes.UUID)
                .withClusteringColumn("insertTime", DataTypes.TIMESTAMP).
                withClusteringOrder("insertTime", ClusteringOrder.DESC);
        executeStatement(createTable.build(), keyspace);
    }

    public void insertArticleToUser(UUID userId, UUID articleId) {
        var a = new ArticleByUser(userId, articleId, Timestamp.from(Instant.now()));
        template.insert(a);
    }

    public List<ArticleByUser> getLast10ArticlesOf(UUID userId) {
        return template.select(Query.query(Criteria.where("userId").is(userId)).limit(10), ArticleByUser.class);
    }
}
