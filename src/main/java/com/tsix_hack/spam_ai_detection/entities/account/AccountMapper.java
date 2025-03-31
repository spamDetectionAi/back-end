package com.tsix_hack.spam_ai_detection.entities.account;

import com.tsix_hack.spam_ai_detection.entities.peopleInfo.PeopleInfoMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = PeopleInfoMapper.class)
public interface AccountMapper {
    AccountMapper INSTANCE = Mappers.getMapper(AccountMapper.class);
    @Mapping(source = "peopleInfo", target = "peopleInfoDTO")
    AccountDTO toDTO(Account account);
    Account toEntity(AccountDTO accountDTO);
}
