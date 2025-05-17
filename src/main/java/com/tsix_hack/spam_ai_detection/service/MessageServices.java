package com.tsix_hack.spam_ai_detection.service;

import com.tsix_hack.spam_ai_detection.configuration.WebSocketSessionListener;
import com.tsix_hack.spam_ai_detection.entities.account.accountForm.Account;
import com.tsix_hack.spam_ai_detection.entities.account.mapper.AccountMapper;
import com.tsix_hack.spam_ai_detection.entities.messages.addressesManagement.AddressClassification;
import com.tsix_hack.spam_ai_detection.entities.messages.addressesManagement.ForeignAddresses;
import com.tsix_hack.spam_ai_detection.entities.messages.addressesManagement.NotFoundAddress;
import com.tsix_hack.spam_ai_detection.entities.messages.mapper.MessageMapper;
import com.tsix_hack.spam_ai_detection.entities.messages.messageForm.*;
import com.tsix_hack.spam_ai_detection.entities.peopleInfo.poepleInfoForm.PeopleInfo;
import com.tsix_hack.spam_ai_detection.repositories.AccountRepository;
import com.tsix_hack.spam_ai_detection.repositories.MessagesRepositories.ForeignAddressesRepository;
import com.tsix_hack.spam_ai_detection.repositories.MessagesRepositories.MessageRepository;
import com.tsix_hack.spam_ai_detection.repositories.MessagesRepositories.NotFoundAddressesRepository;
import com.tsix_hack.spam_ai_detection.repositories.PeopleInfoRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

@Service
@AllArgsConstructor
public class MessageServices {
    private WebSocketSessionListener webSocketSessionListener;
    private PeopleInfoRepository peopleInfoRepository;
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
            Message message = messageRepository.save(msg);
            map.put("id", message.getId());
            map.put("msg", message);

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
    public MessageReceived sendMessage(MessageRequest messageRequest) {
        Account account = accountRepository.findAccountById(messageRequest.getSenderId());
        Map<String, Object> idAndReceivers = saveAction(messageRequest);
        Long id = (Long) idAndReceivers.get("id");
        Set<UUID> localids = (Set<UUID>) idAndReceivers.get("localIds");
        MessageReceived messageToSend = createMessageToSend(messageRequest, account, id, LocalDateTime.now());
        webSocketSessionListener.sendMessageToUser(localids, messageToSend);
        Message msg = (Message) idAndReceivers.get("msg");
        webSocketSessionListener.sendToItself(messageRequest.getSenderId(), createSentMessages(msg));
        return messageToSend;
    }

    private MessageReceived createMessageToSend(MessageRequest messageRequest, Account senderAccount, Long messageId, LocalDateTime sendDateTime) {
        PeopleInfo peopleInfo = senderAccount.getPeopleInfo();
        MessageReceived messageReceived = new MessageReceived(messageId
                , senderAccount.getEmail()
                , peopleInfo.getFirstName()
                , peopleInfo.getLastName()
                , messageRequest.getObject()
                , messageRequest.getBody()
                , Timestamp.valueOf(sendDateTime)) ;
        return messageReceived ;
    }


    public Page<MessageToSend> messagesByReceiver(String token, int pageIndex) {
        UUID id = UUID.fromString(tokenService.uuidDecoded(token));
        Pageable page = PageRequest.of(pageIndex, 9);
        Page<Message> messagesPage = messageRepository.findByReceiverId(id, page);
        List<Message> messages = messagesPage.getContent();
        List<MessageToSend> messageToSends = new ArrayList<>();
        for (Message message : messages) {
            MessageToSend messageToSend = MessageMapper.INSTANCE.toSend(message);
            messageToSend.setAccountDTO(AccountMapper.INSTANCE.toDTO(message.getSender()));
            messageToSend.setId(message.getId());
            messageToSends.add(messageToSend);
        }
        Page<MessageToSend> pageToSend = new PageImpl<>(messageToSends, page, messagesPage.getTotalElements());
        return pageToSend;
    }

    public List<SentMessages> findSent(String token) {
        UUID id = UUID.fromString(tokenService.uuidDecoded(token));
        List<SentMessages> sentMessages = new ArrayList<>();
        List<Message> messages = messageRepository.findMessagesBySender(new Account(id));
        for (Message message : messages) {
            SentMessages sentMessage = createSentMessages(message);
            sentMessages.add(sentMessage);
        }
        return sentMessages;
    }

    private SentMessages createSentMessages(Message message) {
        SentMessages sentMessages = MessageMapper.INSTANCE.toSentMessages(message);
        Set<String> receivers = new HashSet<>();
        if (!message.getReceivers().isEmpty()) {
            receivers.addAll(getOfLocalReceivers(message.getReceivers()));
        }
        if (!message.getForeignReceivers().isEmpty()) {
            receivers.addAll(getOfForeignReceivers(message.getForeignReceivers()));
        }
        sentMessages.setReceivers(receivers);
        return sentMessages;
    }

    private Set<String> getOfLocalReceivers(Set<UUID> local) {
        Set<String> emails = new HashSet<>();

        for (UUID uuid : local) {
            emails.add(accountRepository.findAccountById(uuid).getEmail());
        }

        return emails;
    }

    private Set<String> getOfForeignReceivers(Set<Long> foreign) {
        Set<String> emails = new HashSet<>();
        for (Long id : foreign) {
            emails.add(foreignAddressesRepository.findById(id).get().getAddress());
        }
        return emails;
    }

    public Page<MessageReceived> receivedMessages(String token, int pageIndex) {
        UUID id = UUID.fromString(tokenService.uuidDecoded(token));
        Pageable page = PageRequest.of(pageIndex, 9);
        return messageRepository.findReceivedMessages(id, page);
    }

}
