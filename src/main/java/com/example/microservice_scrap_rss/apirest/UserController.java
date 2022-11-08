package com.example.microservice_scrap_rss.apirest;

import com.example.microservice_scrap_rss.cassandra.FeedByUserRepo;
import com.example.microservice_scrap_rss.cassandra.FeedRepo;
import com.example.microservice_scrap_rss.cassandra.KeyspaceRepository;
import com.example.microservice_scrap_rss.cassandra.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(value = "api/v1/users/")
public class UserController {
    /*
    Should provide a way to find any Article by its Id (DONE)
    Should provide a way to get 10 last articles by a user (DONE)
    Should provide a way to sub to flux (DONE BUT NOT TESTED)
    Should provide a way to unsub to flux (TODO)
    * */
    @Autowired
    private KeyspaceRepository keyspaceRepository;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private FeedRepo feedRepo;
    @Autowired
    private FeedByUserRepo feedByUserRepo;

    @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
    @ResponseBody
    String getUserById(@PathVariable final String userId) {
        keyspaceRepository.useKeyspace("test");
        return userRepo.getUserById(UUID.fromString(userId)).toString();
    }

    @PostMapping(value = "/{userId}/{stringURL}")
    @ResponseBody
    String subTo(@PathVariable final String userId, @PathVariable final String stringURL) {
        keyspaceRepository.useKeyspace("test");
        feedByUserRepo.insertFeedToUser(UUID.fromString(userId),feedRepo.insertFeed(stringURL));
        return "Subbed to new feed";
    }


    @DeleteMapping(value = "/{userId}/{feedId}")
    @ResponseBody
    String unSubTo(@PathVariable final String userId, @PathVariable final String feedId) {
        keyspaceRepository.useKeyspace("test");
        //feedRepo.delete(UUID.fromString(feedId));
        feedByUserRepo.removeFeedFromUser(UUID.fromString(userId), UUID.fromString(feedId));
        return "Subbed to new feed";
    }


    @RequestMapping(value = "/last10/{userId}", method = RequestMethod.GET)
    @ResponseBody
    String getLast10OfUser(@PathVariable final String userId) {
        keyspaceRepository.useKeyspace("test");
        return userRepo.last10(UUID.fromString(userId));
    }
}
