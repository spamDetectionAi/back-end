package com.tsix_hack.spam_ai_detection.entities.messages;

import com.tsix_hack.spam_ai_detection.entities.account.Account;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import java.time.LocalDateTime;
import java.util.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id ;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private Account sender ;

    @NotNull
    @Column(name = "send_date_time")
    private LocalDateTime sendDateTime ;

    private String object ;

    private String body ;

    @OneToMany
    @JoinColumn(name = "message_id")
    private List<Attachement> attachement = new ArrayList<>();

    @ElementCollection
    @CollectionTable
    (
            name = "message_reception",
            joinColumns = @JoinColumn(name = "message_id")
    )
    @Column(name = "receivers")
    private Set<UUID> receivers = new HashSet<>();

    @PrePersist
    protected void create() {
        this.sendDateTime = LocalDateTime.now();
    }

    public Message(@NotNull Account sender, String body ) {
        this.sender = sender;
        this.body = body;
    }

    public Message(@NotNull Account sender, String object, String body, Set<UUID> receivers ) {
        this.sender = sender;
        this.object = object;
        this.body = body;
    }
}
