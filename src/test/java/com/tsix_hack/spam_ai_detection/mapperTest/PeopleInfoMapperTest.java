package com.tsix_hack.spam_ai_detection.mapperTest;

import com.tsix_hack.spam_ai_detection.entities.peopleInfo.mapper.PeopleInfoMapper;
import com.tsix_hack.spam_ai_detection.entities.peopleInfo.poepleInfoForm.Gender;
import com.tsix_hack.spam_ai_detection.entities.peopleInfo.poepleInfoForm.PeopleInfo;
import com.tsix_hack.spam_ai_detection.entities.peopleInfo.poepleInfoForm.PeopleInfoDTO;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PeopleInfoMapperTest {

    private final PeopleInfoMapper mapper = PeopleInfoMapper.INSTANCE;

    @Test
    void map_peopleInfo_toDTO() {
        UUID testId = UUID.randomUUID();
        String testFirstName = "John";
        String testLastName = "Doe";
        java.sql.Date testDateOfBirth = new Date(2000, 1, 1);
        Gender testGender = Gender.MALE;

        PeopleInfo peopleInfo = new PeopleInfo(testId, testFirstName, null, testLastName, testDateOfBirth, testGender);

        PeopleInfoDTO dtoOrigin = new PeopleInfoDTO(testFirstName, null, testLastName, testGender);

        PeopleInfoDTO dto = mapper.toDTO(peopleInfo);

        assertEquals(dtoOrigin, dto);
    }


}