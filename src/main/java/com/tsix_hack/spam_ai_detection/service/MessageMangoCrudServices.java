package com.tsix_hack.spam_ai_detection.service;

import com.tsix_hack.spam_ai_detection.entities.MessagesMango.EntityMessages.MessagesMongoDb;
import com.tsix_hack.spam_ai_detection.repositories.MessagesRepositories.MongoMessageRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class MessageMangoCrudServices {

    private final MongoMessageRepository mongoMessageRepository;

    public List<MessagesMongoDb> sentMessage(String sender){
        return mongoMessageRepository.findSentMessages(sender);
    }

    public Page<MessagesMongoDb> receivedMessages(String receiver , boolean isSpam , int pageIndex){
        Pageable pageable = Pageable.ofSize(10).withPage(pageIndex);
        return mongoMessageRepository.findReceivedMessages(receiver , isSpam , pageable);
    }

    public MessagesMongoDb findById(String id){
        return mongoMessageRepository.findMessagesMongoDbById(id).orElse(null);
    }
}
