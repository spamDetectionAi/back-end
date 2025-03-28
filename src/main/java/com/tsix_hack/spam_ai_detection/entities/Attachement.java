package com.tsix_hack.spam_ai_detection.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.net.URI;
import java.net.URL;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Attachement {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id ;

    @NotNull
    private URL url ;

    @NotNull
    private String type ;
}
