package com.rss_aggregator.cassandra;

import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.UUID;

@Table
public class Person {

  @PrimaryKey
  private final UUID id;

  private final String name;
  private final int age;

  public Person(UUID id, String name, int age) {
    this.id = id;
    this.name = name;
    this.age = age;
  }

  public UUID getId() {
    return id;
  }

  private String getName() {
    return name;
  }

  private int getAge() {
    return age;
  }

  @Override
  public String toString() {
    return String.format("{ @type = %1$s, id = %2$s, name = %3$s, age = %4$d }", getClass().getName(), getId(),
        getName(), getAge());
  }
}