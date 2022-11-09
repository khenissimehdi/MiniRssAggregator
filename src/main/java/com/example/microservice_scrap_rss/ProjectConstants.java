package com.example.microservice_scrap_rss;

public enum ProjectConstants {
    KEYSPACE("test"),
    TABLE_USER("user"),
    TABLE_ARTICLE("article"),
    TABLE_FEED("feed"),
    TABLE_ARTICLE_BY_USER("articlebyuser"),
    TABLE_FEED_BY_ARTICLE("feedbyarticle"),
    TABLE_FEED_BY_USER("feedbyuser"),

    API_URL_DEV("http://localhost:8080/api/v1/articles/save");

    private final String env;

    ProjectConstants(String env) {
        this.env = env;
    }

    public String env() {
        return env;
    }
}
