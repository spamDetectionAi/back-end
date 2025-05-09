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
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@AllArgsConstructor
public class MessageServices {
    private WebSocketSessionListener webSocketSessionListener;
    private AccountRepository accountRepository;
    private MessageRepository messageRepository;
    private TokenService tokenService;

   private Long save(MessageRequest messageRequest) {
       Set<UUID> uuidSet = new HashSet<>();
       for (String mail : messageRequest.getReceivers()){
           Optional<Account> account = accountRepository.findAccountByEmail(mail);
           if (account.isPresent()){
               uuidSet.add(account.get().getId());
           }
       }
       Account account = new Account(messageRequest.getSenderId()) ;
       Message msg = MessageMapper.INSTANCE.toEntity(messageRequest, account);
       msg.setReceivers(uuidSet);
       return messageRepository.save(msg).getId();
   }
    @Transactional
    public MessageToSend sendMessage(MessageRequest messageRequest) {
        Account account = accountRepository.findAccountById(messageRequest.getSenderId());
        Long id = save(messageRequest);
        MessageToSend messageToSend = MessageMapper.INSTANCE.toSend(messageRequest , AccountMapper.INSTANCE.toDTO(account));
        messageToSend.setSendDateTime(LocalDateTime.now());
        messageToSend.setId(id);
        Set<UUID> receivers = new HashSet<>();
        List<String> notFoundEmails = new ArrayList<>();
        for (String email : messageRequest.getReceivers()) {
            Optional<Account> receiver = accountRepository.findAccountByEmail(email);
            if (receiver.isPresent()) {
                receivers.add(receiver.get().getId());
            }else {
                notFoundEmails.add(email);
            }
        }
        webSocketSessionListener.sendMessageToUser(receivers, messageToSend);
        return messageToSend ;
    }

    public List<MessageToSend> messagesByReceiver(String token) {
       UUID id = UUID.fromString(tokenService.uuidDecoded(token));
       List<Message> messages = messageRepository.findByReceiverId(id) ;
       List<MessageToSend> messageToSends = new ArrayList<>();
       for (Message message : messages) {
           MessageToSend messageToSend = MessageMapper.INSTANCE.toSend(message) ;
           messageToSend.setAccountDTO(AccountMapper.INSTANCE.toDTO(message.getSender()));
           messageToSend.setId(message.getId());
           messageToSends.add(messageToSend);
       }
       return messageToSends;
    }
}
