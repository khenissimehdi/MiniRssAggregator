package com.example.microservice_scrap_rss.cassandra.entity;


import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.UUID;

@Table
public class FeedByUser {
    @PrimaryKey
    private final UUID userId;
    private final UUID feedId;

    public FeedByUser(UUID userId, UUID feedId) {
        this.userId = userId;
        this.feedId = feedId;
    }

    public UUID getFeedId() {
        return feedId;
    }
}
