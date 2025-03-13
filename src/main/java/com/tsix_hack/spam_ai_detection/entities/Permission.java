package com.tsix_hack.spam_ai_detection.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id ;

    @NotNull
    @Enumerated(EnumType.STRING)
    private PermissionType permissionType ;
}
