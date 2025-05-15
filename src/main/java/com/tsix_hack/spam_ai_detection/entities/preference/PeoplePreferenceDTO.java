package com.tsix_hack.spam_ai_detection.entities.preference;
import lombok.*;

import java.util.UUID;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PeoplePreferenceDTO {
    private UUID userId;
    private String emailBody;
    private String modelPrediction;
    private String userLabel;
}
