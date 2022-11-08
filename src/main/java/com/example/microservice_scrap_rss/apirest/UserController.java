package com.example.microservice_scrap_rss.apirest;

import com.example.microservice_scrap_rss.cassandra.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "api/v1/users/")
public class UserController {

    private final Gson gson = new Gson();

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

    @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
    @ResponseBody
    String getUserById(@PathVariable final String userId) throws JsonProcessingException {
        keyspaceRepository.useKeyspace("test");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        return objectMapper.writeValueAsString(userRepo.getUserById(UUID.fromString(userId)));
    }

    @RequestMapping(value = "/last10/{userId}", method = RequestMethod.GET, produces= MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String getLast10OfUser(@PathVariable final String userId) throws JsonProcessingException {
        keyspaceRepository.useKeyspace("test");
        var list = articleByUserRepo.getLast10ArticlesOf(UUID.fromString(userId));
        var arr = new ArrayList<String>();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        for(var item : list){
            arr.add(objectMapper.writeValueAsString(articleRepo.getArticleById(item.articleId)));
        }
        return arr.toString();
    }
}
