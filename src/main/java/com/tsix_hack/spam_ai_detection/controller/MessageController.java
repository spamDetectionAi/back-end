package com.tsix_hack.spam_ai_detection.controller;
import com.tsix_hack.spam_ai_detection.entities.MessagesMango.EntityMessages.MessagesMongoDb;
import com.tsix_hack.spam_ai_detection.entities.messages.messageForm.*;
import com.tsix_hack.spam_ai_detection.repositories.MessagesRepositories.MessageRepository;
import com.tsix_hack.spam_ai_detection.service.MessageMangoCrudServices;
import com.tsix_hack.spam_ai_detection.service.MessageServices;
import com.tsix_hack.spam_ai_detection.service.TokenService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/user")
@AllArgsConstructor
public class MessageController {
    private MessageServices messageServices;
    private TokenService tokenService ;
    private MessageRepository messageRepository ;
    private final MessageMangoCrudServices services  ;

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

    @GetMapping ("/sent/{email}")
    public List<MessagesMongoDb> findSentMessages(@PathVariable String email){
        return services.sentMessage(email) ;
    }

    @GetMapping ("/received/{email}/{pageIndex}/{isSpam}")
    public Page<MessagesMongoDb> findReceivedMessages(@PathVariable String email , @PathVariable int pageIndex , @PathVariable boolean isSpam){
        return services.receivedMessages(email , isSpam , pageIndex) ;
    }

    @GetMapping("/byId/{id}")
    public MessagesMongoDb findById(@PathVariable String id){
        return services.findById(id) ;
    }



}
