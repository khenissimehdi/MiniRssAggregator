package com.example.microservice_scrap_rss.controller;

import com.example.microservice_scrap_rss.ProjectConstants;
import com.example.microservice_scrap_rss.cassandra.KeyspaceRepository;
import com.example.microservice_scrap_rss.cassandra.repository.ArticleByUserRepo;
import com.example.microservice_scrap_rss.cassandra.repository.ArticleRepo;
import com.example.microservice_scrap_rss.cassandra.repository.FeedByUserRepo;
import com.example.microservice_scrap_rss.cassandra.repository.UserRepo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.UUID;

@RestController
@RequestMapping(value = "api/v1/users/")
public class UserController {

    private final KeyspaceRepository keyspaceRepository;
    private final UserRepo userRepo;
    private final ArticleRepo articleRepo;
    private final FeedByUserRepo feedByUserRepo;
    private final ArticleByUserRepo articleByUserRepo;

    @Autowired
    UserController(KeyspaceRepository keyspaceRepository, UserRepo userRepo, ArticleRepo articleRepo, FeedByUserRepo feedByUserRepo, ArticleByUserRepo articleByUserRepo) {
        this.keyspaceRepository = keyspaceRepository;
        this.userRepo = userRepo;
        this.articleRepo = articleRepo;
        this.feedByUserRepo = feedByUserRepo;

        this.articleByUserRepo = articleByUserRepo;
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<String> getUserById(@PathVariable final String userId) throws JsonProcessingException {
        if (userRepo.getUserById(UUID.fromString(userId)) == null)
            return new ResponseEntity<>("Id not found", HttpStatus.BAD_REQUEST);
        keyspaceRepository.useKeyspace(ProjectConstants.KEYSPACE.env());
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        var response = objectMapper.writeValueAsString(userRepo.getUserById(UUID.fromString(userId)));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(value = "/subTo/{userId}/{feedId}")
    ResponseEntity<String> subUserToFeed(@PathVariable final String userId, @PathVariable final String feedId) {
        if (userRepo.getUserById(UUID.fromString(userId)) == null)
            return new ResponseEntity<>("Id not found", HttpStatus.BAD_REQUEST);
        keyspaceRepository.useKeyspace(ProjectConstants.KEYSPACE.env());
        feedByUserRepo.insertFeedToUser(UUID.fromString(userId), UUID.fromString(feedId));
        return new ResponseEntity<>("Subbed", HttpStatus.OK);
    }

    @PostMapping(value = "/unSubTo/{userId}/{feedId}")
    ResponseEntity<String> unsubUserToFeed(@PathVariable final String userId, @PathVariable final String feedId) {
        if (userRepo.getUserById(UUID.fromString(userId)) == null)
            return new ResponseEntity<>("Id not found", HttpStatus.BAD_REQUEST);
        keyspaceRepository.useKeyspace(ProjectConstants.KEYSPACE.env());
        feedByUserRepo.removeFeedFromUser(UUID.fromString(userId), UUID.fromString(feedId));
        return new ResponseEntity<>("Unsubbed", HttpStatus.OK);
    }

    @RequestMapping(value = "/last10/{userId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<String> getLast1OArticleByUserId(@PathVariable final String userId) throws JsonProcessingException {
        if (userRepo.getUserById(UUID.fromString(userId)) == null)
            return new ResponseEntity<>("Id not found", HttpStatus.BAD_REQUEST);
        keyspaceRepository.useKeyspace(ProjectConstants.KEYSPACE.env());
        var list = articleByUserRepo.getLast10ArticlesOf(UUID.fromString(userId));
        var arr = new ArrayList<String>();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        for (var item : list)
            arr.add(objectMapper.writeValueAsString(articleRepo.getArticleById(item.articleId)));
        return new ResponseEntity<>(arr.toString(), HttpStatus.OK);
    }
}
