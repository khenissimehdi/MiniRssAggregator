package com.example.microservice_scrap_rss.rssfeedscraper;

public record Answer(String title, String description, String author) {

    @Override
    public String toString() {
        if (author == null)
            return description + "@" + title + " : Not found";
        else
            return description + "@" + title + " : " + author;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Answer)) return false;

        Answer answer = (Answer) o;

        if (!title.equals(answer.title)) return false;
        if (!description.equals(answer.description)) return false;
        return author != null ? author.equals(answer.author) : answer.author == null;
    }

    @Override
    public int hashCode() {
        int result = title.hashCode();
        result = 31 * result + description.hashCode();
        result = 31 * result + (author != null ? author.hashCode() : 0);
        return result;
    }
}