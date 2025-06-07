package com.tsix_hack.spam_ai_detection.entities.MessagesMango.SenderManager;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type" // Ce champ DOIT apparaître dans le JSON
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = ForeignSender.class, name = "email"),
        @JsonSubTypes.Type(value = LocalSender.class, name = "senderId")
})
public sealed interface Sender permits LocalSender, ForeignSender {}
