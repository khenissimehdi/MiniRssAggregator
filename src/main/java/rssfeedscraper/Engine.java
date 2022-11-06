package rssfeedscraper;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.*;

//Bonjour c moi qui lé fé
public class Engine {
    private final ArrayList<String> sites;

    private List<Callable<List<Optional<Answer>>>> cal;

    private final int timeoutMilliGlobal;
    private final  ExecutorService pool;

    private Engine(ArrayList<String> sites, int timeoutMilliGlobal, int poolSize) {
        this.sites = sites;
        this.timeoutMilliGlobal = timeoutMilliGlobal;
        this.pool = Executors.newFixedThreadPool(poolSize);
    }

    public static Engine createEngineFromFile(Path path,  int timeoutMilliGlobal, int poolSize) throws IOException {
        Objects.requireNonNull(path);
        var sites = Files.readAllLines(path);
        if(sites.isEmpty()){
            throw new IllegalStateException("file is empty");
        }

        return new Engine(new ArrayList<>(sites),timeoutMilliGlobal,poolSize);
    }

    private Callable<List<Optional<Answer>>> performReq(String site) {
        try {
            var request = new URL(site);
            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feed = input.build(new XmlReader(request));
            List<Optional<Answer>> articles = new ArrayList<>();
           return () -> {

                feed.getEntries().forEach(e->articles.add(Optional.of(new Answer(e.getTitle(), e.getDescription().toString(), e.getAuthor()))));
                return articles;

            };
        } catch (FeedException | IOException e) {
            return ()->List.of(Optional.empty());
        }

    }
    private static Answer mapToArticle(SyndEntry entry){
        return new Answer(entry.getTitle(),entry.getDescription().getValue(),entry.getAuthor());
    }
    private void initCalls() {
        this.cal = sites.stream().parallel().map(this::performReq).toList();
    }

    public ArrayList<Optional<Answer>> retrieve() throws InterruptedException {
        ArrayList<Optional<Answer>> list = new ArrayList<>();
        if (cal == null) {
            initCalls();
        }
        var future = this.pool.invokeAll(cal, timeoutMilliGlobal, TimeUnit.MILLISECONDS);
        this.pool.shutdown();
        future.forEach(e -> {
            try {
                var answers = e.get();
                list.addAll(answers);
            } catch (InterruptedException | ExecutionException ex) {
                throw new AssertionError();
            }
        });

        return new ArrayList<>(list);
    }

    public static void main(String[] args) throws InterruptedException, IOException {
        var agregator = Engine.createEngineFromFile(Path.of("feeds.txt"),400,150);
        var answer = agregator.retrieve();
        System.out.println(answer);
    }
}