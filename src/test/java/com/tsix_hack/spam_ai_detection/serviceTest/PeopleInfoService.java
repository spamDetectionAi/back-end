package com.tsix_hack.spam_ai_detection.serviceTest;

import com.tsix_hack.spam_ai_detection.entities.peopleInfo.poepleInfoForm.Gender;
import com.tsix_hack.spam_ai_detection.entities.peopleInfo.poepleInfoForm.PeopleInfo;
import com.tsix_hack.spam_ai_detection.repositories.PeopleInfoRepository;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.sql.Date;
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
public class PeopleInfoService {
    @Autowired
    private PeopleInfoRepository peopleInfoRepository ;

    @Test
    void save_and_check_if_exists() {
        var peopleInfo = new PeopleInfo(null , "tsiory" , null , "ras" , Date.valueOf(LocalDate.now()) , Gender.MALE ) ;
        UUID ID = peopleInfoRepository.save(peopleInfo).getId() ;
        var get = peopleInfoRepository.findById(ID) ;

        assertEquals(ID , get.get().getId()) ;
    }
}
