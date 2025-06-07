package com.tsix_hack.spam_ai_detection.entities.MessagesMango.EntityMessages;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MessageSender {
    String email;
    EmailType type;
}
