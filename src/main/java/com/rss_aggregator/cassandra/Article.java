package com.rss_aggregator.cassandra;

public record Article(int id, String title, String description, String pubDate, String link, int guId) {
}
