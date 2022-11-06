package com.example.microservice_scrap_rss.cassandra;

import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.*;

@Table
public class User {

    @PrimaryKey
    private final UUID id;
    private ArrayList<UUID> listarticles;

    public User(UUID id) {
        this.id = id;
        listarticles=new ArrayList<>();
    }

    public UUID getId() {
        return id;
    }

    @Override
    public String toString() {
        return "User " + id + " articles : " + listarticles;
    }

    public void subscribe(UUID idArticle){
        if(listarticles==null) listarticles=new ArrayList<>();
        Collections.addAll(listarticles,idArticle);
    }

    public List<UUID> last10() {
        var diff=listarticles.size()-10;
        if(diff<0) diff=0;
        return listarticles.subList(diff, listarticles.size());
    }
}