package com.tsix_hack.spam_ai_detection.entities.messages;

import com.tsix_hack.spam_ai_detection.entities.account.Account;
import com.tsix_hack.spam_ai_detection.entities.account.AccountDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MessageMapper {
    MessageMapper INSTANCE = Mappers.getMapper(MessageMapper.class);

    MessageToSend toSend(MessageRequest messageRequest , AccountDTO accountDTO);
    
    @Mapping(source = "accountDTO", target = "sender")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "sendDateTime", ignore = true)
    @Mapping(target = "receivers", ignore = true)
    Message toEntity(MessageRequest messageRequest , Account accountDTO);

    @Mapping(source = "sender", target = "accountDTO")
    MessageToSend toSend(Message message);
}
