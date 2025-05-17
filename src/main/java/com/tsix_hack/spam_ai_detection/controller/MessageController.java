package com.tsix_hack.spam_ai_detection.controller;
import com.tsix_hack.spam_ai_detection.entities.messages.messageForm.*;
import com.tsix_hack.spam_ai_detection.repositories.MessagesRepositories.MessageRepository;
import com.tsix_hack.spam_ai_detection.service.MessageServices;
import com.tsix_hack.spam_ai_detection.service.TokenService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
public class MessageController {
    private MessageServices messageServices;
    private TokenService tokenService ;
    private MessageRepository messageRepository ;

    @PostMapping("/send")
    public void messageSend(@RequestBody MessageRequest messageRequest , @RequestHeader("Authorization") String token) {
        messageRequest.setSenderId(UUID.fromString(tokenService.uuidDecoded(token)));
        messageServices.sendMessage(messageRequest);
    }

    @GetMapping("/messages/{page}")
    public Page<MessageReceived> findReceivedMessages(@RequestHeader("Authorization") String token , @PathVariable int page){
        return messageServices.receivedMessages(token , page) ;
    }

    @GetMapping("/sent")
    public List<SentMessages> findBySender(@RequestHeader("Authorization") String token){
        return messageServices.findSent(token) ;
    }



}
