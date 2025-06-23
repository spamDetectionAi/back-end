package com.tsix_hack.spam_ai_detection.controller;

import com.tsix_hack.spam_ai_detection.entities.MessagesMango.EntityMessages.MessagesMongoDb;
import com.tsix_hack.spam_ai_detection.entities.MessagesMango.SenderManager.MessageRequest;
import com.tsix_hack.spam_ai_detection.service.Detection;
import com.tsix_hack.spam_ai_detection.service.MessageMangoServices;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class Ping {

    private final Detection detection;
    private final MessageMangoServices services ;

    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }



    @PostMapping("/insert")
    public MessagesMongoDb save(@RequestBody MessageRequest messageRequest){
        return services.sendMessage(messageRequest);
    }
}
