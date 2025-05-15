package com.tsix_hack.spam_ai_detection.repositories.MessagesRepositories;

import com.tsix_hack.spam_ai_detection.entities.messages.messageForm.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, Long> {
    Message findById(long id);
    @Query("SELECT m FROM Message m WHERE :receiverId IN elements(m.receivers) order by m.sendDateTime desc ")
    List<Message> findByReceiverId(@Param("receiverId") UUID receiverId);
}
