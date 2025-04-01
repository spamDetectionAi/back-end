package com.tsix_hack.spam_ai_detection.controller;
import com.tsix_hack.spam_ai_detection.configuration.WebSocketSessionListener;
import com.tsix_hack.spam_ai_detection.entities.messages.MessageRequest;
import com.tsix_hack.spam_ai_detection.entities.messages.MessageToSend;
import com.tsix_hack.spam_ai_detection.service.MessageServices;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@Controller
@AllArgsConstructor
public class MessageController {
    private MessageServices messageServices;

    @MessageMapping("/chat/private")
    public ResponseEntity<MessageToSend> sendPrivateMessage(@NotNull @Payload MessageRequest message) {
       return ResponseEntity
               .status(HttpStatus.OK)
               .body(messageServices.sendMessage(message));
    }

    @MessageMapping("/test")
    @SendTo("/topic/public")
    public String test(@Payload String message){
        System.out.println("message: " + message);
        return message;
    }

    @GetMapping("/messages/{id}")
    public ResponseEntity<List<MessageToSend>> getAllMessages(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(messageServices.messagesByReceiver(id)) ;
    }


}
