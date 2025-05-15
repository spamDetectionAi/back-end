package com.tsix_hack.spam_ai_detection.mapperTest;

import com.tsix_hack.spam_ai_detection.entities.account.accountForm.Account;
import com.tsix_hack.spam_ai_detection.entities.account.accountForm.AccountDTO;
import com.tsix_hack.spam_ai_detection.entities.account.mapper.AccountMapper;
import com.tsix_hack.spam_ai_detection.entities.account.permissonForm.Permission;
import com.tsix_hack.spam_ai_detection.entities.account.permissonForm.PermissionType;
import com.tsix_hack.spam_ai_detection.entities.peopleInfo.mapper.PeopleInfoMapper;
import com.tsix_hack.spam_ai_detection.entities.peopleInfo.poepleInfoForm.Gender;
import com.tsix_hack.spam_ai_detection.entities.peopleInfo.poepleInfoForm.PeopleInfo;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AccountMapperTest {
    private final AccountMapper mapper = AccountMapper.INSTANCE ;
    private final PeopleInfoMapper peopleInfoMapper = PeopleInfoMapper.INSTANCE ;

    @Test
    public void map_account_toDTO() {
        UUID testId = UUID.randomUUID();
        String testFirstName = "John";
        String testLastName = "Doe";
        java.sql.Date testDateOfBirth = new Date(2000, 1, 1);
        Gender testGender = Gender.MALE;

        var peopleInfo = new PeopleInfo(testId, testFirstName, null, testLastName, testDateOfBirth, testGender);
        var permission = new Permission(1 , PermissionType.USER) ;

        UUID uuid = UUID.randomUUID() ;
        String email = "tsiory@maily.tech" ;
        String phone = "0600000000" ;

        var account = new Account(uuid ,peopleInfo , permission , phone , email , "pass1235") ;
        var dtoOrigin = new AccountDTO(email , peopleInfoMapper.toDTO(peopleInfo) ) ;
        var dto = mapper.toDTO(account);

        assertEquals(dtoOrigin , dto) ;
    }
}
