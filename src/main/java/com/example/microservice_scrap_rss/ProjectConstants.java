package com.example.microservice_scrap_rss;

public enum ProjectConstants {
    KEYSPACE("test"),
    USER("user");
    
    private final String env;

    ProjectConstants(String env) {
        this.env = env;
    }

    public String env() {
        return env;
    }
}
