package com.tsix_hack.spam_ai_detection.controller;
import com.tsix_hack.spam_ai_detection.configuration.WebSocketSessionListener;
import com.tsix_hack.spam_ai_detection.entities.MessageRequest;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.web.bind.annotation.RestController;



@RestController
@AllArgsConstructor
public class MessageController {
    private final WebSocketSessionListener webSocketSessionListener;

    @MessageMapping("/chat/private")
    public MessageRequest sendPrivateMessage(@NotNull @Payload MessageRequest message) {
      return webSocketSessionListener.sendMessageToUser(message);

    }

    @MessageMapping("/test")
    @SendTo("/topic/public")
    public String test(@Payload String message){
        System.out.println("message: " + message);
        return message;
    }



}
