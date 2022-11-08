package com.example.microservice_scrap_rss.cassandra;


import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

public record Article(UUID id, String title, String description, LocalDate pubDate, String link) {
}
