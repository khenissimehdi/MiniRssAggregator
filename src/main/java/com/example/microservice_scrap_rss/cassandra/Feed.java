package com.example.microservice_scrap_rss.cassandra;


import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.net.URL;
import java.util.UUID;

//TODO CLEAN THE CODE
@Table
public class Feed {
    @PrimaryKey
    private final UUID feedId;
    public final String feedLink;


    public Feed(UUID feedId, String feedLink) {
        this.feedId = feedId;
        this.feedLink = feedLink;
    }
}
