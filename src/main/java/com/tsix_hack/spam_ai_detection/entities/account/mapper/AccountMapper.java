package com.tsix_hack.spam_ai_detection.entities.account.mapper;

import com.tsix_hack.spam_ai_detection.entities.account.accountForm.Account;
import com.tsix_hack.spam_ai_detection.entities.account.accountForm.AccountDTO;
import com.tsix_hack.spam_ai_detection.entities.peopleInfo.mapper.PeopleInfoMapper;
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
