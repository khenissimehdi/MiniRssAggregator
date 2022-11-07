package com.example.microservice_scrap_rss.rssfeedscraper;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RSSReader {

    public static List<Answer> read(String feedLink) throws IOException, FeedException {
        URL source = new URL(feedLink);
        SyndFeedInput input = new SyndFeedInput();
        SyndFeed feed = input.build(new XmlReader(source));
        List<Answer> articles = new ArrayList<>();
        feed.getEntries().forEach(e-> articles.add(mapToArticle(e)));
        return articles;
    }

    private static Answer mapToArticle(SyndEntry entry){
        return new Answer(UUID.randomUUID(),entry.getTitle(),entry.getDescription().getValue(),entry.getUpdatedDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),entry.getLink());
    }

    public static void main(String[] args) throws IOException, FeedException {
        var sites = getListFromLines(Path.of("feeds.txt"));
        for(var url:sites){
            var answers = RSSReader.read(url);
            System.out.println("for url = "+url+ " we found\n");
            answers.forEach(System.out::println);
            System.out.println("-------------------------------------------------------------------");
        }
    }

    static List<String> getListFromLines(Path path) throws IOException {
        var sites = Files.readAllLines(path);
        if(sites.isEmpty())
            throw new IllegalStateException("file is empty");
        return sites;
    }
}
