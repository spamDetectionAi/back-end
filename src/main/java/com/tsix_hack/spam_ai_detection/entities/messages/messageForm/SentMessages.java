package com.tsix_hack.spam_ai_detection.entities.messages.messageForm;

import lombok.*;

import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class SentMessages {
    private String object ;
    private String body ;
    private Set<String> receivers ;
}
