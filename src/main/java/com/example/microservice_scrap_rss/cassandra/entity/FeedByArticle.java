package com.example.microservice_scrap_rss.cassandra.entity;


import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.UUID;

@Table
public class FeedByArticle {
    @PrimaryKey
    private final UUID feedId;
    private final UUID articleId;

    public FeedByArticle(UUID feedId, UUID articleId) {
        this.feedId = feedId;
        this.articleId = articleId;
    }

    public UUID getArticleId() {
        return articleId;
    }
}
