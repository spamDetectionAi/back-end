package com.tsix_hack.spam_ai_detection.repositories.MessagesRepositories;

import com.tsix_hack.spam_ai_detection.entities.MessagesMango.EntityMessages.MessagesMongoDb;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MongoMessageRepository extends MongoRepository<MessagesMongoDb, String> {
}
