package com.example.microservice_scrap_rss.cassandra;

import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.ArrayList;
import java.util.UUID;

@Table
public class User {

    @PrimaryKey
    private final UUID id;
    private final ArrayList<UUID> listarticles = new ArrayList<>();

    public User(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    @Override
    public String toString() {
        return "User " + id + " articles : " + listarticles;
    }

    public void subscribe(UUID idArticle){listarticles.add(idArticle);}

    public String last10() {
        return listarticles.subList(listarticles.size() - 10, listarticles.size()).toString();
    }
}