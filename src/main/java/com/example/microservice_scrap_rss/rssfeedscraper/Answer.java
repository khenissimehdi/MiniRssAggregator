package com.example.microservice_scrap_rss.rssfeedscraper;

import java.time.LocalDate;
import java.util.UUID;

public record Answer(UUID id , String title, String description, LocalDate pubDate, String link) {

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", pubDate=" + pubDate +
                ", link='" + link + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Answer answer = (Answer) o;

        if (!id.equals(answer.id)) return false;
        if (!title.equals(answer.title)) return false;
        if (!description.equals(answer.description)) return false;
        if (!pubDate.equals(answer.pubDate)) return false;
        return link.equals(answer.link);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + title.hashCode();
        result = 31 * result + description.hashCode();
        result = 31 * result + pubDate.hashCode();
        result = 31 * result + link.hashCode();
        return result;
    }
}