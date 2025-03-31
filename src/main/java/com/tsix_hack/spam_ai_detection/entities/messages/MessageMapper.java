package com.tsix_hack.spam_ai_detection.entities.messages;

import com.tsix_hack.spam_ai_detection.entities.account.AccountDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MessageMapper {
    MessageMapper INSTANCE = Mappers.getMapper(MessageMapper.class);
    MessageToSend toSend(MessageRequest messageRequest , AccountDTO accountDTO);
}
