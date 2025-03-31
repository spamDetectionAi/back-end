package com.tsix_hack.spam_ai_detection.service;

import com.tsix_hack.spam_ai_detection.configuration.WebSocketSessionListener;
import com.tsix_hack.spam_ai_detection.entities.account.Account;
import com.tsix_hack.spam_ai_detection.entities.account.AccountMapper;
import com.tsix_hack.spam_ai_detection.entities.messages.MessageMapper;
import com.tsix_hack.spam_ai_detection.entities.messages.MessageRequest;
import com.tsix_hack.spam_ai_detection.entities.messages.MessageToSend;
import com.tsix_hack.spam_ai_detection.repositories.AccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MessageServices {
    private WebSocketSessionListener webSocketSessionListener;
    private AccountRepository accountRepository;

    public MessageToSend sendMessage(MessageRequest messageRequest) {
        Account account = accountRepository.findAccountById(messageRequest.getSenderId());
        MessageToSend messageToSend = MessageMapper.INSTANCE.toSend(messageRequest , AccountMapper.INSTANCE.toDTO(account)) ;
        webSocketSessionListener.sendMessageToUser(messageRequest.getReceivers(), messageToSend);
        return messageToSend ;
    }
}
