package com.example.microservice_scrap_rss.cassandra.entity;

import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.UUID;

@Table
public class Feed {
    @PrimaryKey
    private final UUID feedId;
    private final String feedLink;

    public Feed(UUID feedId, String feedLink) {
        this.feedId = feedId;
        this.feedLink = feedLink;
    }

    public UUID getFeedId() {
        return feedId;
    }

    public String getFeedLink() {
        return feedLink;
    }
}
