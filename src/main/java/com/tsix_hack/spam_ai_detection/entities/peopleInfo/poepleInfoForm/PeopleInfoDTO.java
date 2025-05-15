package com.tsix_hack.spam_ai_detection.entities.peopleInfo.poepleInfoForm;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class PeopleInfoDTO {
    private String firstName ;
    private String SecondName ;
    private String lastName ;
    private Gender gender ;
}
