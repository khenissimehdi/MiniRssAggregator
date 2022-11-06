package com.rss_aggregator.entity;

public record Article(int id, String title, String description, String pubDate, String link, int guId) {
}
