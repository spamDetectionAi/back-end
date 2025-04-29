package com.tsix_hack.spam_ai_detection.entities.messages;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.net.URL;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Attachment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id ;

    @NotNull
    private String originalFilename ;

    @NotNull
    private String type ;

    @NotNull
    @Column(unique = true)
    private String key ;
}
