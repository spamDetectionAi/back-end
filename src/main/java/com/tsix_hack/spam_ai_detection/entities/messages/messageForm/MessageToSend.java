package com.tsix_hack.spam_ai_detection.entities.messages.messageForm;

import com.tsix_hack.spam_ai_detection.entities.account.accountForm.AccountDTO;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class MessageToSend {
    private Long id;
    private AccountDTO accountDTO;
    private String object ;
    private String body ;
    private LocalDateTime sendDateTime ;
}
