package com.tsix_hack.spam_ai_detection.entities.messages;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

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
