package com.tsix_hack.spam_ai_detection.entities.messages.messageForm;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@Getter
@Setter
public class MessageRequest {
    private UUID senderId;
    private String object ;
    private String body ;
    private Set<String> receivers ;
}
