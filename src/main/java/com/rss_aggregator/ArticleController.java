package com.rss_aggregator;

import com.rss_aggregator.entity.Article;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rssfeedscraper.Answer;

import java.util.List;

@RestController
@RequestMapping("api/v1/messages")
public class ArticleController {

    private KafkaTemplate<String,Answer> kafkaTemplate;

    public ArticleController(KafkaTemplate<String, Answer> kafkaTemplate){
        this.kafkaTemplate = kafkaTemplate;
    }

    @PostMapping
    public void publish(@RequestBody ArticleRequest request){
        kafkaTemplate.send("rss",request.answer());
    }
}
