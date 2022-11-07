package com.example.microservice_scrap_rss.apirest;

import com.example.microservice_scrap_rss.cassandra.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
    @ResponseBody
    String getUserById(@PathVariable final String userId) {
        keyspaceRepository.useKeyspace("test");
        return userRepo.getUserById(UUID.fromString(userId)).toString();
    }

    @RequestMapping(value = "/last10/{userId}", method = RequestMethod.GET)
    @ResponseBody
    String getLast10OfUser(@PathVariable final String userId) {
        keyspaceRepository.useKeyspace("test");
        return articleByUserRepo.getLast10ArticlesOf(UUID.fromString(userId)).toString();
    }
}
