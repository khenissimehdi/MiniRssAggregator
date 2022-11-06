package com.example.microservice_scrap_rss.apirest;

import com.example.microservice_scrap_rss.cassandra.KeyspaceRepository;
import com.example.microservice_scrap_rss.cassandra.UserRepo;
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

    @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
    @ResponseBody
    String getBookById(@PathVariable final String userId) {
        keyspaceRepository.useKeyspace("test");
        var user = userRepo.getUserById(UUID.fromString(userId));
        return user.toString();
     }
}
