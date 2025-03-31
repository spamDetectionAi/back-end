package com.tsix_hack.spam_ai_detection.entities.account;

import com.tsix_hack.spam_ai_detection.entities.peopleInfo.PeopleInfoDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class AccountDTO {
    private String email;
    private PeopleInfoDTO peopleInfoDTO;
}
