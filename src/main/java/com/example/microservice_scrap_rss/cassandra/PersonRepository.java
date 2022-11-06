package com.example.microservice_scrap_rss.cassandra;


import org.springframework.stereotype.Repository;

@Repository
public class PersonRepository {
//    private final CqlSession session;
//    private final CassandraOperations template;
//
//    public PersonRepository(CqlSession session) {
//        this.session = session;
//        this.template = new CassandraTemplate(session);
//    }
//
//    private ResultSet executeStatement(SimpleStatement statement, String keyspace) {
//        if (keyspace != null)
//            statement.setKeyspace(CqlIdentifier.fromCql(keyspace));
//        return session.execute(statement);
//    }
//
//    public void createTable(String keyspace) {
//        CreateTable createTable = SchemaBuilder.createTable("person").ifNotExists()
//                .withPartitionKey("id", DataTypes.TEXT)
//                .withColumn("name", DataTypes.TEXT)
//                .withColumn("age", DataTypes.INT);
//        executeStatement(createTable.build(), keyspace);
//    }
//
//    public void insertPerson(String name, int age) {
//        template.insert(new Person(UUID.randomUUID(), name, age));
//    }
//
//    public Person getPersonById(UUID uuid) {
//        return template.selectOne(Query.query(Criteria.where("id").is(uuid)), Person.class);
//    }
}
