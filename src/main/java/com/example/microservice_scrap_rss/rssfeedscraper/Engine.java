package com.example.microservice_scrap_rss.rssfeedscraper;

import com.example.microservice_scrap_rss.cassandra.Feed;
import com.example.microservice_scrap_rss.cassandra.FeedByArticleRepo;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.*;
public class Engine {

    private final List<Feed> sites;
    private List<Callable<List<Optional<Answer>>>> callableList;
    private final int timeoutMilliGlobal;
    private final ExecutorService executorService;
    private final FeedByArticleRepo feedByArticleRepo;


    private Engine(List<Feed> sites,FeedByArticleRepo feedByArticleRepo,int timeoutMilliGlobal, int poolSize) {
        this.sites = sites;
        this.timeoutMilliGlobal = timeoutMilliGlobal;
        this.executorService = Executors.newFixedThreadPool(poolSize);
        this.feedByArticleRepo = feedByArticleRepo;
    }

    public static Engine createEngineFromList(List<Feed> sites, FeedByArticleRepo feedByArticleRepo,int timeoutMilliGlobal, int poolSize) {
        return new Engine(sites,feedByArticleRepo,timeoutMilliGlobal,poolSize);
    }

    private Callable<List<Optional<Answer>>> performReq(Feed site) {
        try {
            var request = new URL(site.getFeedLink());
            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feed = input.build(new XmlReader(request));
            List<Optional<Answer>> articles = new ArrayList<>();

           return () -> {
                feed.getEntries().forEach(e-> {
                    var pubDate = LocalDate.ofInstant(e.getPublishedDate().toInstant(), ZoneId.systemDefault());
                    var uuid = UUID.randomUUID();
                    feedByArticleRepo.insertArticleToFeed(site.getFeedId(),uuid);

                    articles.add(Optional.of(
                            new Answer(uuid,
                            e.getTitle(),
                            e.getDescription().getValue(),
                           pubDate,e.getLink())));
                }
                );

                return articles;
            };
        } catch (FeedException | IOException e) {
            return ()->List.of(Optional.empty());
        }
    }

    private void initCalls() {
        this.callableList = sites.stream().parallel().map(this::performReq).toList();
    }

    public List<Optional<Answer>> retrieve() throws InterruptedException {
        ArrayList<Optional<Answer>> list = new ArrayList<>();
        if (callableList == null)
            initCalls();
        var future = this.executorService.invokeAll(callableList, timeoutMilliGlobal, TimeUnit.MILLISECONDS);
        this.executorService.shutdown();

        future.forEach(e -> {
            try {
                var answers = e.get();
                list.addAll(answers);
            } catch (InterruptedException | ExecutionException ex) {
                throw new AssertionError(ex.getMessage());
            }
        });
        return new ArrayList<>(list);
    }
}