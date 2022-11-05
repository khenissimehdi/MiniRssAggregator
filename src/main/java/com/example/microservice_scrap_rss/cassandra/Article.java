package com.example.microservice_scrap_rss.cassandra;


import java.util.UUID;

public record Article(UUID id, String title, String description, String pubDate, String link) {
}
