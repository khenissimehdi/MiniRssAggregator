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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.*;

public class Engine {
    private final List<String> sites;
    private List<Callable<List<Optional<Answer>>>> callableList;
    private final int timeoutMilliGlobal;
    private final ExecutorService executorService;

    private Engine(List<String> sites, int timeoutMilliGlobal, int poolSize) {
        this.sites = sites;
        this.timeoutMilliGlobal = timeoutMilliGlobal;
        this.executorService = Executors.newFixedThreadPool(poolSize);
    }

    public static Engine createEngineFromList(List<String> sites, int timeoutMilliGlobal, int poolSize) {
        return new Engine(sites,timeoutMilliGlobal,poolSize);
    }

    public static Engine createEngineFromFile(Path path,  int timeoutMilliGlobal, int poolSize) throws IOException {
        Objects.requireNonNull(path);
        var sites = Files.readAllLines(path);
        if(sites.isEmpty())
            throw new IllegalStateException("file is empty");
        return new Engine(new ArrayList<>(sites),timeoutMilliGlobal,poolSize);
    }

    private Callable<List<Optional<Answer>>> performReq(String site) {
        try {
            var request = new URL(site);
            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feed = input.build(new XmlReader(request));
            List<Optional<Answer>> articles = new ArrayList<>();
           return () -> {

                feed.getEntries().forEach(e-> {
                    var pubDate = LocalDate.ofInstant(e.getPublishedDate().toInstant(), ZoneId.systemDefault());
                    articles.add(Optional.of(
                            new Answer(UUID.randomUUID(),
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

    private static Answer mapToArticle(SyndEntry entry){
        return new Answer(UUID.randomUUID(),entry.getTitle(),entry.getDescription().getValue(),entry.getUpdatedDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),entry.getLink());
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

    public static void main(String[] args) throws InterruptedException, IOException {
        var aggregator = Engine.createEngineFromFile(Path.of("feeds.txt"),400,150);
        var answer = aggregator.retrieve();
        System.out.println(answer);
    }
}