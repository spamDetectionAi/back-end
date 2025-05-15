package com.tsix_hack.spam_ai_detection.entities.preference;

import com.tsix_hack.spam_ai_detection.entities.account.accountForm.Account;
import com.tsix_hack.spam_ai_detection.entities.peopleInfo.poepleInfoForm.PeopleInfo;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "people_preference")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PeoplePreference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Account user;

    @Column(name = "email_body", columnDefinition = "TEXT", nullable = false)
    private String emailBody;

    @Column(name = "model_prediction")
    private String modelPrediction;

    @Column(name = "user_label")
    private String userLabel;

    @Column(name = "created_at")
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}
