package com.tsix_hack.spam_ai_detection.entities.messages;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MessageMapper {
    MessageMapper INSTANCE = Mappers.getMapper(MessageMapper.class);
    MessageToSend requestToSend(MessageRequest messageRequest);
    Message toEntity(MessageRequest messageRequest);
}
