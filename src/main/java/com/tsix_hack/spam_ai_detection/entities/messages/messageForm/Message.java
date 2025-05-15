package com.tsix_hack.spam_ai_detection.entities.messages.messageForm;

import com.tsix_hack.spam_ai_detection.entities.account.accountForm.Account;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import java.time.LocalDateTime;
import java.util.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
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

    @Column(columnDefinition = "TEXT")
    private String body ;

    @OneToMany
    @JoinColumn(name = "message_id")
    private List<Attachment> attachement = new ArrayList<>();

    @ElementCollection
    @CollectionTable
    (
            name = "message_reception",
            joinColumns = @JoinColumn(name = "message_id")
    )
    @Column(name = "receivers")
    private Set<UUID> receivers = new HashSet<>();

    @ElementCollection
    @CollectionTable
    (
            name = "foreign_messages",
            joinColumns = @JoinColumn(name = "message_id")
    )
    @Column(name = "foreign_receiver_id")
    private Set<Long> foreignReceivers = new HashSet<>();

    @ElementCollection
    @CollectionTable
            (
                    name = "not_found_receivers",
                    joinColumns = @JoinColumn(name = "message_id")
            )
    @Column(name = "not_found_receiver_id")
    private Set<Long> notFoundReceivers = new HashSet<>();

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
