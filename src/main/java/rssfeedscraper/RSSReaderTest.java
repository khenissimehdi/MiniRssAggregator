package rssfeedscraper;

import com.rometools.rome.io.FeedException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Objects;

public class RSSReaderTest {

    public static void main(String[] args) throws IOException, FeedException {
        var sites = Files.readAllLines(Path.of("feeds.txt"));
        if(sites.isEmpty()){
            throw new IllegalStateException("file is empty");
        }
        System.out.println("sites :"+sites.toString());

        for(var url:sites){
            var answers = RSSReader.read(url);
            System.out.println("for url = "+url+ "we found\n");
            answers.stream().forEach(System.out::println);
            System.out.println("-------------------------------------------------------------------");
        }
    }
}
