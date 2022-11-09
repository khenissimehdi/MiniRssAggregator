package com.example.microservice_scrap_rss.controller;

import com.example.microservice_scrap_rss.ProjectConstants;
import com.example.microservice_scrap_rss.cassandra.KeyspaceRepository;
import com.example.microservice_scrap_rss.cassandra.repository.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.UUID;

@RestController
@RequestMapping(value = "api/v1/users/")
public class UserController {

    @Autowired
    private KeyspaceRepository keyspaceRepository;

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private ArticleRepo articleRepo;

    @Autowired
    private FeedByUserRepo feedByUserRepo;

    @Autowired
    private ArticleByUserRepo articleByUserRepo;

    @Autowired
    private FeedRepo feedRepo;

    @RequestMapping(value = "/{userId}", method = RequestMethod.GET, produces= MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String getUserById(@PathVariable final String userId) throws JsonProcessingException {
        keyspaceRepository.useKeyspace(ProjectConstants.KEYSPACE.env());
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        return objectMapper.writeValueAsString(userRepo.getUserById(UUID.fromString(userId)));
    }

    @PostMapping(value = "/subTo/{userId}/{feedId}")
    String subUserToFeed(@PathVariable final String userId,@PathVariable final String feedId) throws JsonProcessingException {
        keyspaceRepository.useKeyspace(ProjectConstants.KEYSPACE.env());
        feedByUserRepo.insertFeedToUser(UUID.fromString(userId),UUID.fromString(feedId));
        return "Subbed !";
    }

    @PostMapping(value = "/unSubTo/{userId}/{feedId}")
    String unSubUserToFeed(@PathVariable final String userId,@PathVariable final String feedId) throws JsonProcessingException {
        keyspaceRepository.useKeyspace(ProjectConstants.KEYSPACE.env());
        feedByUserRepo.removeFeedFromUser(UUID.fromString(userId),UUID.fromString(feedId));
        return "UnSubbed !";
    }

    @RequestMapping(value = "/last10/{userId}", method = RequestMethod.GET, produces= MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String getLast1OArticleByUserId(@PathVariable final String userId) throws JsonProcessingException {
        keyspaceRepository.useKeyspace(ProjectConstants.KEYSPACE.env());
        var list = articleByUserRepo.getLast10ArticlesOf(UUID.fromString(userId));
        var arr = new ArrayList<String>();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        for(var item : list)
            arr.add(objectMapper.writeValueAsString(articleRepo.getArticleById(item.articleId)));
        return arr.toString();
    }
}
