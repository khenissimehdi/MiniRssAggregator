package com.rss_aggregator.repository;


import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.core.type.DataTypes;
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder;
import com.datastax.oss.driver.api.querybuilder.schema.CreateTable;
import com.rss_aggregator.entity.Person;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.data.cassandra.core.query.Criteria;
import org.springframework.data.cassandra.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class PersonRepository {
    private final CqlSession session;
    private final CassandraOperations template;

    public PersonRepository(CqlSession session) {
        this.session = session;
        this.template = new CassandraTemplate(session);
    }

    private ResultSet executeStatement(SimpleStatement statement, String keyspace) {
        if (keyspace != null)
            statement.setKeyspace(CqlIdentifier.fromCql(keyspace));
        return session.execute(statement);
    }

    public void createTable(String keyspace) {
        CreateTable createTable = SchemaBuilder.createTable("person").ifNotExists()
                .withPartitionKey("id", DataTypes.TEXT)
                .withColumn("name", DataTypes.TEXT)
                .withColumn("age", DataTypes.INT);
        executeStatement(createTable.build(), keyspace);
    }



    public void insertPerson(String name, int age) {
        template.insert(new Person(UUID.randomUUID(), name, age));
    }

    public Person getPersonById(UUID uuid) {
        return template.selectOne(Query.query(Criteria.where("id").is(uuid)), Person.class);
    }
}
