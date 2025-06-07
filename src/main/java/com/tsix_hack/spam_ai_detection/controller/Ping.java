package com.tsix_hack.spam_ai_detection.controller;

import com.tsix_hack.spam_ai_detection.entities.MessagesMango.SenderManager.MessageRequest;
import com.tsix_hack.spam_ai_detection.service.Detection;
import com.tsix_hack.spam_ai_detection.service.MessageMangoServices;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@AllArgsConstructor
public class Ping {

    private final Detection detection;
    private final MessageMangoServices services ;

    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }

    @PostMapping("/check-spam")
    public String checkSpam(@RequestParam String text) {
        Mono<Boolean> res =  detection.callDetector(text) ;
        if (res.block()) {
            return "Spam detected" ;
        }
        return "No Spam detected" ;
    }

    @PostMapping("/insert")
    public void save(@RequestBody MessageRequest messageRequest){
        services.sendMessage(messageRequest);
    }
}
