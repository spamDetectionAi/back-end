package com.tsix_hack.spam_ai_detection.entities.MessagesMango.EntityMessages;

import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Document(collection = "mail")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MessagesMongoDb {
    @Id
    private String id;
    private String object;
    private String body;
    private Instant sendDateTime;
    @NotNull
    private MessageSender messageSender;
    @NotNull
    private List<Receiver> receivers;

    public MessagesMongoDb( String object, String body, MessageSender messageSender, List<Receiver> receivers) {
        this.object = object;
        this.body = body;
        this.sendDateTime = Instant.now();
        this.messageSender = messageSender;
        this.receivers = receivers;
    }
}
