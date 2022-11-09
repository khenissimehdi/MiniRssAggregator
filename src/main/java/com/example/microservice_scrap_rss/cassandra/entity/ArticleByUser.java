package com.example.microservice_scrap_rss.cassandra.entity;


import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.sql.Timestamp;
import java.util.UUID;
@Table
public class ArticleByUser {
    @PrimaryKey
    private final UUID userId;
    public final UUID articleId;
    private final Timestamp insertTime;

    public ArticleByUser(UUID userId, UUID articleId, Timestamp insertTime) {
        this.userId = userId;
        this.articleId = articleId;
        this.insertTime = insertTime;
    }

    @Override
    public String toString() {
        return "{" +
                "userId=" + userId +
                ", articleId=" + articleId +
                ", insertTime=" + insertTime +
                '}';
    }
}
