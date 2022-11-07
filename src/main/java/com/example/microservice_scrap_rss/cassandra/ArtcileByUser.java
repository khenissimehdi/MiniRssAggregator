package com.example.microservice_scrap_rss.cassandra;


import com.datastax.oss.driver.api.core.type.DataTypes;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.UUID;

//TODO CLEAN THE CODE
@Table
public class ArtcileByUser {
    @PrimaryKey
    private final UUID userId;
    public final UUID articleId;

    private final Timestamp insertTime;

    public ArtcileByUser(UUID userId, UUID articleId, Timestamp insertTime) {
        this.userId = userId;
        this.articleId = articleId;
        this.insertTime = insertTime;
    }
}
