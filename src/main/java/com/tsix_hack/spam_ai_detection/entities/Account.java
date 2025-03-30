package com.tsix_hack.spam_ai_detection.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Account {
    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id ;

    @ManyToOne
    @JoinColumn(name = "peopleInfo_id")
    private PeopleInfo peopleInfo;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Permission permission;

    @NotNull
    @Column(name = "phone_number")
    private String phoneNumber ;

    @NotNull
    @Column(unique = true)
    @Email
    private String email ;

    @NotNull
    @Size(min = 6)
    private String password ;

    public void setAccountPermission(int id) {
        this.permission = new Permission(id);
    }

    public void setAccountPassword(String password) {
        this.password = password;
    }

}
