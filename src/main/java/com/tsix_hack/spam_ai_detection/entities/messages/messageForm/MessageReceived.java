package com.tsix_hack.spam_ai_detection.entities.messages.messageForm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageReceived {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String object;
    private String body;
    private Timestamp sendDateTime;

}
