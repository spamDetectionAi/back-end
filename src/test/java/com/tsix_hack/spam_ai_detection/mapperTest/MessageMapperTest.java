package com.tsix_hack.spam_ai_detection.mapperTest;

import com.tsix_hack.spam_ai_detection.entities.account.accountForm.Account;
import com.tsix_hack.spam_ai_detection.entities.account.accountForm.AccountDTO;
import com.tsix_hack.spam_ai_detection.entities.messages.mapper.MessageMapper;
import com.tsix_hack.spam_ai_detection.entities.messages.messageForm.Message;
import com.tsix_hack.spam_ai_detection.entities.messages.messageForm.MessageRequest;
import com.tsix_hack.spam_ai_detection.entities.messages.messageForm.MessageToSend;
import com.tsix_hack.spam_ai_detection.entities.messages.messageForm.SentMessages;
import com.tsix_hack.spam_ai_detection.entities.peopleInfo.poepleInfoForm.Gender;
import com.tsix_hack.spam_ai_detection.entities.peopleInfo.poepleInfoForm.PeopleInfoDTO;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MessageMapperTest {
    MessageMapper mapper = MessageMapper.INSTANCE;

    @Test
    void map_messageRequest_to_message_to_send_to_the_destination() {

        UUID senderId = UUID.randomUUID();
        String object = "object";
        String body = "hello there";
        LocalDateTime sendDateTime = LocalDateTime.now();
        Set<String> receivers = Set.of("tsiory@maily.tech", "achraf@maily.tech");
        AccountDTO accountDTO = new AccountDTO("account@maily.tech",
                new PeopleInfoDTO("tsiory",
                        null,
                        "ras",
                        Gender.MALE));
        var MessageRequest = new MessageRequest(senderId, object, body, receivers);
        var MessageToSendMapped = mapper.toSend(MessageRequest, accountDTO);
        MessageToSendMapped.setId(2000000L);
        MessageToSendMapped.setSendDateTime(sendDateTime);
        var MessageToSend = new MessageToSend(2000000L, accountDTO, object, body, sendDateTime);

        assertEquals(MessageToSend, MessageToSendMapped);
    }

    @Test
    void mep_message_to_sent_message() {
        Account sender = new Account("tsiory@maily.tech");


        Set<UUID> receivers = Set.of(UUID.randomUUID(), UUID.randomUUID());

        Set<String> emails = Set.of("hello@gmail.com") ;
        String object = "Hello World";
        String body = "This is a test message body.";


        Message message = new Message(sender, object, body, receivers);
        SentMessages sentMessages = mapper.toSentMessages(message);
        sentMessages.setReceivers(emails);
        System.out.println(sentMessages);


    }
}
