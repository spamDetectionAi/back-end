package com.tsix_hack.spam_ai_detection.entities.messages;

import com.tsix_hack.spam_ai_detection.entities.account.AccountDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MessageToSend {
    private Long id;
    private AccountDTO accountDTO;
    private String object ;
    private String body ;
    private LocalDateTime sendDateTime ;
}
