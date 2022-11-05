package com.example.microservice_scrap_rss.cassandra;

import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.ArrayList;
import java.util.UUID;

@Table
public class User {

    @PrimaryKey
    private final UUID id;
    private final ArrayList<Integer> listArticles = new ArrayList<>();

    public User(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    @Override
    public String toString() {
        return "User " + id + " articles : " + listArticles;
    }

    public void subscribe(int idArticle){listArticles.add(idArticle);
    }

    public String last10() {
        return listArticles.subList(listArticles.size() - 10, listArticles.size()).toString();
    }
}