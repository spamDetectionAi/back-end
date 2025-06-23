package com.tsix_hack.spam_ai_detection.repositories.MessagesRepositories;

import com.tsix_hack.spam_ai_detection.entities.MessagesMango.EntityMessages.MessagesMongoDb;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface MongoMessageRepository extends MongoRepository<MessagesMongoDb, String> {
    @Query(value = "{'messageSender.email': ?0}" , sort = "{'sendDateTime': -1}")
    List<MessagesMongoDb> findSentMessages(String sender) ;

    @Query(value = "{ 'isSpam': ?1, 'receivers': { $elemMatch: { 'email': ?0, 'status': 'SENT' } }  }", sort = "{'sendDateTime': -1}")
    public Page<MessagesMongoDb> findReceivedMessages(String receiver , boolean isSpam, Pageable pageable) ;

    Optional<MessagesMongoDb> findMessagesMongoDbById(String id);

}
