package com.example.microservice_scrap_rss.cassandra;

import io.javalin.Javalin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;


import java.util.UUID;

@Component
public class RunAfterStartup {

    @Autowired
    private KeyspaceRepository keyspaceRepository;

    @Autowired
    private UserRepo userRepo;

    @EventListener(ApplicationReadyEvent.class)
    public void runAfterStartup() {
//        keyspaceRepository.createKeyspace("test", 1);
        keyspaceRepository.useKeyspace("test");
//        userRepo.createTable("user");
//        userRepo.insertUser();
//        userRepo.subscribe(UUID.fromString("65750552-c807-479f-b0d3-31a2da175a05"),UUID.randomUUID());
    }
//    @EventListener(ApplicationReadyEvent.class)
//    public void api(){
//        try(var app = Javalin.create(/*config*/)){
//            app.get("/", ctx -> ctx.result("Available queries :\n" +
//                            "/last10/userid={userid} (get)\n" +
//                            "/article/article={id} (get)\n" +
//                            "/save/{article} (post)"))
//                    .get("/last10/{userid}",ctx->ctx.result(userRepo.last10(UUID.fromString(ctx.pathParam("userid")))))
//                    .get("/article/{id}",ctx->ctx.result("Article :{id}"))
//                    .start(7070);
//        }
//    }


}