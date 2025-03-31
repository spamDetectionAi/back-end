package com.tsix_hack.spam_ai_detection.entities.peopleInfo;

import com.tsix_hack.spam_ai_detection.entities.account.Account;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.UUID;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PeopleInfoDTO {
    private String firstName ;
    private String SecondName ;
    private String lastName ;
    private Gender gender ;
}
