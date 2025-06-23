package com.tsix_hack.spam_ai_detection.entities.MessagesMango.EntityMessages;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Receiver {
    private String email;
    private EmailType type;
    private Status status;
}
