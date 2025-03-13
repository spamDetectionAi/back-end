package com.tsix_hack.spam_ai_detection.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.UUID;
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
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
    private String email ;

    @NotNull
    @Size(min = 6)
    private String password ;

    @ManyToMany
    @JoinTable(
            name = "message_reception" ,
            joinColumns = @JoinColumn(name = "account_receiver") ,
            inverseJoinColumns = @JoinColumn(name = "message_received")
    )


    private HashSet<Message> messageReceived ;

    public void setAccountPermission(int id) {
        this.permission = new Permission(id);
    }
}
