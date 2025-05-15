package com.tsix_hack.spam_ai_detection.entities.account.accountForm;

import com.tsix_hack.spam_ai_detection.entities.peopleInfo.poepleInfoForm.PeopleInfoDTO;
import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class AccountDTO {
    private String email;
    private PeopleInfoDTO peopleInfoDTO;
}
