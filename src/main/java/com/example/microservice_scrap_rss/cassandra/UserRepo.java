package com.example.microservice_scrap_rss.cassandra;


import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.core.type.DataTypes;
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder;
import com.datastax.oss.driver.api.querybuilder.schema.CreateTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.data.cassandra.core.query.Criteria;
import org.springframework.data.cassandra.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class UserRepo {
    private final CqlSession session;
    private final CassandraOperations template;

    @Autowired
    private ArticleRepo articleRepo;
    public UserRepo(CqlSession session) {
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
        CreateTable createTable = SchemaBuilder.createTable("user").ifNotExists()
                .withPartitionKey("id", DataTypes.UUID)
                .withColumn("listarticles", DataTypes.listOf(DataTypes.UUID));
        executeStatement(createTable.build(), keyspace);
    }



    public void insertUser() {
        template.insert(new User(UUID.randomUUID()));
    }

    public User getUserById(UUID uuid) {
        return template.selectOne(Query.query(Criteria.where("id").is(uuid)), User.class);
    }

    public void subscribe(UUID uuid,UUID idArticle){
        User us=getUserById(uuid);
        us.subscribe(idArticle);
        template.update(us);
    }

    public String last10(UUID uuid) {
        var sb=new StringBuilder();
        sb.append("{\n");
        var list = getUserById(uuid).last10();
        for(var articleId : list){
            sb.append(articleRepo.getArticleById(articleId)).append("\n");
        }
        sb.append("}");
        return sb.toString();
    }
}

