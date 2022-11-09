package com.example.microservice_scrap_rss;

public enum ProjectConstants {
    KEYSPACE("test"),
    TABLE_USER("user"),
    TABLE_ARTICLE("article"),
    TABLE_FEED("feed"),
    TABLE_ARTICLE_BY_USER("articleByUser"),
    TABLE_FEED_BY_ARTICLE("feedByArticle"),
    TABLE_FEED_BY_USER("feedByUser"),
    API_URL_DEV("http://localhost:8080/api/v1");

    private final String env;

    ProjectConstants(String env) {
        this.env = env;
    }

    public String env() {
        return env;
    }
}
