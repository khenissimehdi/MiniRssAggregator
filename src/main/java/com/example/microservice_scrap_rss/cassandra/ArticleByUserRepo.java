package com.example.microservice_scrap_rss.cassandra;


import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.core.metadata.schema.ClusteringOrder;
import com.datastax.oss.driver.api.core.type.DataTypes;
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder;
import com.datastax.oss.driver.api.querybuilder.schema.CreateTable;
import com.datastax.oss.driver.api.querybuilder.schema.CreateTableWithOptions;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.data.cassandra.core.query.Columns;
import org.springframework.data.cassandra.core.query.Criteria;
import org.springframework.data.cassandra.core.query.Query;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public class ArticleByUserRepo {
    private final CqlSession session;
    private final CassandraOperations template;


    public ArticleByUserRepo(CqlSession session) {
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
        CreateTableWithOptions createTable = SchemaBuilder.createTable("artcilebyuser").ifNotExists()
                .withPartitionKey("userId", DataTypes.UUID)
                .withPartitionKey("articleId", DataTypes.UUID)
                .withClusteringColumn("inserttime", DataTypes.TIMESTAMP).
                withClusteringOrder("inserttime", ClusteringOrder.DESC);
        executeStatement(createTable.build(), keyspace);
    }



    public ArtcileByUser insertArticleToUser(UUID userId, UUID articleId) {
        var a= new ArtcileByUser(userId,articleId,Timestamp.from(Instant.now()));
        template.insert(a);
        return a;
    }

    public List<ArtcileByUser> getLast10ArticlesOf(UUID userId) {
        // To do sorting
        return template.select(Query.query(Criteria.where("userid").is(userId)).columns(Columns.from("articleid")).limit(10), ArtcileByUser.class);
    }
}
