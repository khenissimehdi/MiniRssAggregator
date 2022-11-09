package com.example.microservice_scrap_rss.cassandra.entity;


import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.UUID;

//TODO CLEAN THE CODE
@Table
public class FeedByUser {
    @PrimaryKey
    private final UUID userId;

    public UUID getUserId() {
        return userId;
    }

    public UUID getFeedId() {
        return feedId;
    }

    private final UUID feedId;


    public FeedByUser(UUID userId, UUID feedId) {
        this.userId = userId;
        this.feedId = feedId;
    }
}
