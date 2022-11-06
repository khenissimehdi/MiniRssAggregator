package com.example.microservice_scrap_rss.cassandra;


import java.time.LocalDate;
import java.util.UUID;

public record Article(UUID id, String title, String description, LocalDate pubDate, String link) {
    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", pubDate=" + pubDate +
                ", link='" + link + '\'' +
                '}';
    }
}
