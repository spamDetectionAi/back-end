package com.tsix_hack.spam_ai_detection.entities.peopleInfo.poepleInfoForm;

import jakarta.persistence.*;
import lombok.*;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.sql.Date;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Table(name = "people_info")
@EqualsAndHashCode
@ToString
public class PeopleInfo implements Serializable {
    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id ;

    @Column(nullable = false)
    private String firstName ;

    private String SecondName ;

    private String lastName ;

    @Column(name = "date_of_birth", nullable = false)
    private Date dateOfBirth ;

    @Column(nullable = false)
    @NotNull
    @Enumerated(EnumType.STRING)
    private Gender gender ;

}


