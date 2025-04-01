package com.tsix_hack.spam_ai_detection.service;

import com.tsix_hack.spam_ai_detection.configuration.WebSocketSessionListener;
import com.tsix_hack.spam_ai_detection.entities.account.Account;
import com.tsix_hack.spam_ai_detection.entities.account.AccountMapper;
import com.tsix_hack.spam_ai_detection.entities.messages.Message;
import com.tsix_hack.spam_ai_detection.entities.messages.MessageMapper;
import com.tsix_hack.spam_ai_detection.entities.messages.MessageRequest;
import com.tsix_hack.spam_ai_detection.entities.messages.MessageToSend;
import com.tsix_hack.spam_ai_detection.repositories.AccountRepository;
import com.tsix_hack.spam_ai_detection.repositories.MessageRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@AllArgsConstructor
public class MessageServices {
    private WebSocketSessionListener webSocketSessionListener;
    private AccountRepository accountRepository;
    private MessageRepository messageRepository;

   private Message save(MessageRequest messageRequest) {
       Account account = new Account(messageRequest.getSenderId()) ;
       Message msg = MessageMapper.INSTANCE.toEntity(messageRequest, account);
       messageRepository.save(msg);
       return msg;
   }

    public MessageToSend sendMessage(MessageRequest messageRequest) {
        Account account = accountRepository.findAccountById(messageRequest.getSenderId());
        save(messageRequest);
        MessageToSend messageToSend = MessageMapper.INSTANCE.toSend(messageRequest , AccountMapper.INSTANCE.toDTO(account));
        messageToSend.setSendDateTime(LocalDateTime.now());
        webSocketSessionListener.sendMessageToUser(messageRequest.getReceivers(), messageToSend);
        return messageToSend ;
    }

    public List<MessageToSend> messagesByReceiver(UUID receiver) {
       List<Message> messages = messageRepository.findByReceiverId(receiver) ;
       List<MessageToSend> messageToSends = new ArrayList<>();
       for (Message message : messages) {
           MessageToSend messageToSend = MessageMapper.INSTANCE.toSend(message) ;
           messageToSend.setAccountDTO(AccountMapper.INSTANCE.toDTO(message.getSender()));
           messageToSends.add(messageToSend);
       }
       return messageToSends;
    }
}
