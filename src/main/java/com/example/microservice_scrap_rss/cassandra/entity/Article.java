package com.example.microservice_scrap_rss.cassandra.entity;


import java.time.LocalDate;
import java.util.UUID;

public record Article(UUID id, String title, String description, LocalDate pubDate, String link) {
}
