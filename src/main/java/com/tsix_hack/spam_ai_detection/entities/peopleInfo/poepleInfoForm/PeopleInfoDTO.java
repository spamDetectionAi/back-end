package com.tsix_hack.spam_ai_detection.entities.peopleInfo.poepleInfoForm;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
