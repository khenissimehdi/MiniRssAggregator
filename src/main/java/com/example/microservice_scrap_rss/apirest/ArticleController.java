package com.example.microservice_scrap_rss.apirest;

import com.example.microservice_scrap_rss.ProjectConstants;
import com.example.microservice_scrap_rss.cassandra.ArticleRepo;
import com.example.microservice_scrap_rss.cassandra.KeyspaceRepository;
import com.example.microservice_scrap_rss.rssfeedscraper.Answer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "api/v1/articles/")
public class ArticleController {

    @Autowired
    private KeyspaceRepository keyspaceRepository;

    @Autowired
    private ArticleRepo articleRepo;

    private KafkaTemplate<String, List<Answer>> kafkaTemplate;

    public ArticleController(KafkaTemplate<String, List<Answer>> kafkaTemplate){
        this.kafkaTemplate = kafkaTemplate;
    }

    @RequestMapping(value = "/{articleId}", method = RequestMethod.GET, produces= MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String getArticleById(@PathVariable final String articleId) throws JsonProcessingException {
        keyspaceRepository.useKeyspace(ProjectConstants.KEYSPACE.env());
        var article = articleRepo.getArticleById(UUID.fromString(articleId));
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        return objectMapper.writeValueAsString(articleRepo.getArticleById(article.id()));
    }

    @PostMapping(value = "/save")
    public void save(@RequestBody ArrayList<Answer> request){
        kafkaTemplate.send("rss",request);
    }
}