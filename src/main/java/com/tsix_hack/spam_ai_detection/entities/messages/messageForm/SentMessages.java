package com.tsix_hack.spam_ai_detection.entities.messages.messageForm;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class SentMessages {
    private Long id ;
    private String object ;
    private String body ;
    private LocalDateTime sendDateTime ;
    private Set<String> receivers ;
}
