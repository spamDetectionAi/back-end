package com.tsix_hack.spam_ai_detection.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;

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

    @ManyToOne
    @JoinColumn(name = "attachement_id")
    private Attachement attachement ;

    @PrePersist
    protected void create() {
        this.sendDateTime = LocalDateTime.now();
    }
}
