package com.example.microservice_scrap_rss.cassandra;

public record Article(int id, String title, String description, String pubDate, String link, int guId) {
}
