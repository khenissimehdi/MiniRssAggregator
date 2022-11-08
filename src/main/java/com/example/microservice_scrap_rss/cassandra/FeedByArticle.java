package com.example.microservice_scrap_rss.cassandra;


import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.UUID;

@Table
public class FeedByArticle {
    @PrimaryKey
    private final UUID feedId;

    public UUID getFeedId() {
        return feedId;
    }

    public UUID getArticleId() {
        return articleId;
    }

    private final UUID articleId;


    public FeedByArticle( UUID feedId,UUID articleId) {
        this.feedId = feedId;
        this.articleId = articleId;
    }
}
