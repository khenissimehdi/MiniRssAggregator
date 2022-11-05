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
import org.springframework.data.cassandra.core.query.Criteria;
import org.springframework.data.cassandra.core.query.Query;
import org.springframework.stereotype.Repository;

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
        CreateTable createTable = SchemaBuilder.createTable("article").ifNotExists()
                .withPartitionKey("id", DataTypes.INT)
                .withColumn("title", DataTypes.TEXT)
                .withColumn("description", DataTypes.TEXT)
                .withColumn("pubDate",DataTypes.DATE)
                .withColumn("link", DataTypes.TEXT);
        executeStatement(createTable.build(), keyspace);
    }



    public void insertArticle(String title,String description, String pubDate, String link) {
        template.insert(new Article(UUID.randomUUID(),title,description,pubDate,link));
    }

    public User getPersonById(UUID uuid) {
        return template.selectOne(Query.query(Criteria.where("id").is(uuid)), User.class);
    }
}
