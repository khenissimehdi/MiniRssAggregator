package com.example.microservice_scrap_rss.cassandra.entity;

import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.UUID;

@Table
public class User {
    @PrimaryKey
    private final UUID id;

    public User(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }
}