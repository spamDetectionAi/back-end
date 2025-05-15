package com.tsix_hack.spam_ai_detection.service;

import com.tsix_hack.spam_ai_detection.configuration.WebSocketSessionListener;
import com.tsix_hack.spam_ai_detection.entities.account.accountForm.Account;
import com.tsix_hack.spam_ai_detection.entities.account.mapper.AccountMapper;
import com.tsix_hack.spam_ai_detection.entities.messages.addressesManagement.AddressClassification;
import com.tsix_hack.spam_ai_detection.entities.messages.addressesManagement.ForeignAddresses;
import com.tsix_hack.spam_ai_detection.entities.messages.addressesManagement.NotFoundAddress;
import com.tsix_hack.spam_ai_detection.entities.messages.mapper.MessageMapper;
import com.tsix_hack.spam_ai_detection.entities.messages.messageForm.Message;
import com.tsix_hack.spam_ai_detection.entities.messages.messageForm.MessageRequest;
import com.tsix_hack.spam_ai_detection.entities.messages.messageForm.MessageToSend;
import com.tsix_hack.spam_ai_detection.repositories.AccountRepository;
import com.tsix_hack.spam_ai_detection.repositories.MessagesRepositories.ForeignAddressesRepository;
import com.tsix_hack.spam_ai_detection.repositories.MessagesRepositories.MessageRepository;
import com.tsix_hack.spam_ai_detection.repositories.MessagesRepositories.NotFoundAddressesRepository;
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
    private ForeignAddressesRepository foreignAddressesRepository;
    private NotFoundAddressesRepository notFoundAddressesRepository;

    private Map<String, Object> saveAction(MessageRequest messageRequest) {
        Map<String, Object> map = new HashMap<>();
        if (!messageRequest.getReceivers().isEmpty()) {
            AddressClassification addressClassification = new AddressClassification(messageRequest.getReceivers(), accountRepository);
            Set<Long> foreignIds = foreignAddressesId(addressClassification);
            Set<Long> notFoundIds = notFoundAddressesId(addressClassification);
            Set<UUID> localIds = localAddressId(addressClassification);
            map.put("localIds", localIds);
            Message msg = MessageMapper.INSTANCE.toEntity(messageRequest, new Account(messageRequest.getSenderId()));
            if (!localIds.isEmpty()) msg.setReceivers(localIds);
            if (!foreignIds.isEmpty()) msg.setForeignReceivers(foreignIds);
            if (!notFoundIds.isEmpty()) msg.setNotFoundReceivers(notFoundIds);
            Long id = messageRepository.save(msg).getId();
            map.put("id", id);
        }


        return map;
    }

    private Set<Long> foreignAddressesId(AddressClassification addressClassification) {
        Set<String> foreignAddressesId = addressClassification.getForeignAddresses();
        Set<Long> foreignAddressesIdLong = new HashSet<>();
        for (String address : foreignAddressesId) {
            Optional<ForeignAddresses> foreignAddresses = foreignAddressesRepository.findByAddress(address);
            if (foreignAddresses.isPresent()) {
                foreignAddressesIdLong.add(foreignAddresses.get().getId());
            } else {
                foreignAddressesIdLong
                        .add(foreignAddressesRepository
                                .save(new ForeignAddresses(address))
                                .getId());
            }
        }
        return foreignAddressesIdLong;
    }

    private Set<Long> notFoundAddressesId(AddressClassification addressClassification) {
        Set<String> notFoundAddresses = addressClassification.getNotFoundAddresses();
        Set<Long> notFoundAddressIds = new HashSet<>();
        for (String address : notFoundAddresses) {
            Optional<NotFoundAddress> notFoundAddress = notFoundAddressesRepository.findIdByAddress(address);
            if (notFoundAddress.isPresent()) {
                notFoundAddressIds.add(notFoundAddress.get().getId());
            } else {
                notFoundAddressIds
                        .add(notFoundAddressesRepository
                                .save(new NotFoundAddress(address))
                                .getId());
            }
        }
        return notFoundAddressIds;
    }

    private Set<UUID> localAddressId(AddressClassification addressClassification) {
        return addressClassification.getUUIDs();
    }

    @Transactional
    public MessageToSend sendMessage(MessageRequest messageRequest) {
        Account account = accountRepository.findAccountById(messageRequest.getSenderId());
        Map<String, Object> idAndReceivers = saveAction(messageRequest);
        Long id = (Long) idAndReceivers.get("id");
        Set<UUID> localids = (Set<UUID>) idAndReceivers.get("localIds");
        MessageToSend messageToSend = createMessageToSend(messageRequest, account, id, LocalDateTime.now());
        webSocketSessionListener.sendMessageToUser(localids, messageToSend);
        return messageToSend;
    }

    private MessageToSend createMessageToSend(MessageRequest messageRequest, Account senderAccount, Long messageId, LocalDateTime sendDateTime) {
        MessageToSend messageToSend = MessageMapper.INSTANCE.toSend(messageRequest, AccountMapper.INSTANCE.toDTO(senderAccount));
        messageToSend.setSendDateTime(sendDateTime);
        messageToSend.setId(messageId);
        return messageToSend;
    }


    public List<MessageToSend> messagesByReceiver(String token) {
        UUID id = UUID.fromString(tokenService.uuidDecoded(token));
        List<Message> messages = messageRepository.findByReceiverId(id);
        List<MessageToSend> messageToSends = new ArrayList<>();
        for (Message message : messages) {
            MessageToSend messageToSend = MessageMapper.INSTANCE.toSend(message);
            messageToSend.setAccountDTO(AccountMapper.INSTANCE.toDTO(message.getSender()));
            messageToSend.setId(message.getId());
            messageToSends.add(messageToSend);
        }
        return messageToSends;
    }
}
