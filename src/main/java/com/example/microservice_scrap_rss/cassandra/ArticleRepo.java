package com.example.microservice_scrap_rss.cassandra;


import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.core.type.DataTypes;
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder;
import com.datastax.oss.driver.api.querybuilder.schema.CreateTable;
import com.example.microservice_scrap_rss.ProjectConstants;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.data.cassandra.core.query.Criteria;
import org.springframework.data.cassandra.core.query.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.UUID;

@Repository
public class ArticleRepo {
    private final CqlSession session;
    private final CassandraOperations template;


    public ArticleRepo(CqlSession session) {
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
        CreateTable createTable = SchemaBuilder.createTable(ProjectConstants.TABLE_ARTICLE.env()).ifNotExists()
                .withPartitionKey("id", DataTypes.UUID)
                .withColumn("title", DataTypes.TEXT)
                .withColumn("description", DataTypes.TEXT)
                .withColumn("pubDate",DataTypes.DATE)
                .withColumn("link", DataTypes.TEXT);
        executeStatement(createTable.build(), keyspace);
    }

    public UUID insertArticle(UUID id, String title, String description, LocalDate date, String link) {
        var a=new Article(id,title,description,date,link);
        template.insert(a);
        return a.id();
    }

    public Article getArticleById(UUID uuid) {
        return template.selectOne(Query.query(Criteria.where("id").is(uuid)), Article.class);
    }
}
