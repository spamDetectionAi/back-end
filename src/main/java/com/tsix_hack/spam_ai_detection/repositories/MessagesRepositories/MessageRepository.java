package com.tsix_hack.spam_ai_detection.repositories.MessagesRepositories;

import com.tsix_hack.spam_ai_detection.entities.account.accountForm.Account;
import com.tsix_hack.spam_ai_detection.entities.messages.messageForm.Message;
import com.tsix_hack.spam_ai_detection.entities.messages.messageForm.MessageReceived;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, Long> {
    Message findById(long id);
    @Query("SELECT m FROM Message m WHERE :receiverId IN elements(m.receivers) order by m.sendDateTime desc ")
    Page<Message> findByReceiverId(@Param("receiverId") UUID receiverId , Pageable pageable);



    @Query("SELECT m FROM Message m WHERE m.id = :id order by m.sendDateTime desc ")
    List<Message> findMessagesByIdOrderBySendDateTimeDesc(Long id) ;

    @Query("SELECT m FROM Message m WHERE m.sender = :account order by m.sendDateTime desc ")
    List<Message> findMessagesBySender(Account account) ;

    @Query(value = """
        SELECT m.id AS id, a.email AS email, 
               p.first_name AS firstName, p.last_name AS lastName, 
               m.object AS object, m.body AS body, 
               m.send_date_time AS sendDateTime
        FROM message_reception mr
        INNER JOIN message m ON mr.message_id = m.id
        INNER JOIN account a ON m.sender_id = a.id
        INNER JOIN people_info p ON a.people_info_id = p.id
        WHERE mr.receivers = :receiverId
        ORDER BY m.send_date_time DESC
    """, nativeQuery = true)
    Page<MessageReceived> findReceivedMessages(@Param("receiverId") UUID receiverId , Pageable pageable);

    /*select m.id , a.email, p.first_name , p.last_name, m."object" , m.body , m.send_date_time from message_reception mr
inner join message m on mr.message_id = m.id
inner join account a on m.sender_id = a.id
inner join people_info p
on a.people_info_id = p.id
where mr.receivers = 'c2cf3276-391b-472b-97a0-fd6ef1187945'*/

}
