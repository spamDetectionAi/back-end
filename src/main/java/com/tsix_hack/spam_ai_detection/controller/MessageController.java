package com.tsix_hack.spam_ai_detection.controller;
import com.tsix_hack.spam_ai_detection.entities.messages.messageForm.MessageRequest;
import com.tsix_hack.spam_ai_detection.entities.messages.messageForm.MessageToSend;
import com.tsix_hack.spam_ai_detection.service.MessageServices;
import com.tsix_hack.spam_ai_detection.service.TokenService;
import lombok.AllArgsConstructor;
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

    @GetMapping("/messages")
    public ResponseEntity<List<MessageToSend>> getAllMessages(@RequestHeader("Authorization") String token) {
        return ResponseEntity.status(HttpStatus.OK).body(messageServices.messagesByReceiver(token)) ;
    }

    @PostMapping("/send")
    public void messageSend(@RequestBody MessageRequest messageRequest , @RequestHeader("Authorization") String token) {
        messageRequest.setSenderId(UUID.fromString(tokenService.uuidDecoded(token)));
        messageServices.sendMessage(messageRequest);
    }

}
