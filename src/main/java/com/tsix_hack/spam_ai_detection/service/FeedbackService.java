package com.tsix_hack.spam_ai_detection.service;

import com.tsix_hack.spam_ai_detection.entities.preference.PeoplePreference;
import com.tsix_hack.spam_ai_detection.entities.preference.PeoplePreferenceDTO;
import com.tsix_hack.spam_ai_detection.entities.peopleInfo.PeopleInfo;
import com.tsix_hack.spam_ai_detection.exceptions.FeedbackProcessingException;
import com.tsix_hack.spam_ai_detection.exceptions.UserNotFoundException;
import com.tsix_hack.spam_ai_detection.repositories.PeoplePreferenceRepository;
import com.tsix_hack.spam_ai_detection.repositories.PeopleInfoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class FeedbackService {

    private static final String SPAM_LABEL = "spam";
    private static final String HAM_LABEL = "ham";

    private final PeoplePreferenceRepository preferenceRepository;
    private final PeopleInfoRepository peopleInfoRepository;
    private final RestTemplate restTemplate;
    private final String fastApiUrl;

    public FeedbackService(
            PeoplePreferenceRepository preferenceRepository,
            PeopleInfoRepository peopleInfoRepository,
            RestTemplate restTemplate,
            @Value("${fastapi.feedback.url}") String fastApiUrl) {
        this.preferenceRepository = preferenceRepository;
        this.peopleInfoRepository = peopleInfoRepository;
        this.restTemplate = restTemplate;
        this.fastApiUrl = fastApiUrl;
    }

    public PeoplePreference saveFeedbackToDB(PeoplePreferenceDTO dto) {
        PeopleInfo user = peopleInfoRepository.findById(dto.getUserId())
                .orElseThrow(() -> new UserNotFoundException(dto.getUserId()));

        validateFeedbackInput(dto);

        PeoplePreference preference = PeoplePreference.builder()
                .user(user)
                .emailBody(dto.getEmailBody())
                .modelPrediction(dto.getModelPrediction())
                .userLabel(dto.getUserLabel().toLowerCase())
                .build();

        return preferenceRepository.save(preference);
    }

    public void sendFeedbackToFastAPI(PeoplePreferenceDTO dto) {
        try {
            Map<String, Object> payload = createFeedbackPayload(dto);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(payload, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    fastApiUrl,
                    HttpMethod.POST,
                    requestEntity,
                    String.class);

            log.info("Feedback sent to FastAPI. Response status: {}", response.getStatusCode());
            log.debug("FastAPI response body: {}", response.getBody());

        } catch (RestClientException e) {
            log.error("Failed to send feedback to FastAPI", e);
            throw new FeedbackProcessingException("Failed to communicate with FastAPI service", e);
        }
    }

    private Map<String, Object> createFeedbackPayload(PeoplePreferenceDTO dto) {
        int preferredLabel = SPAM_LABEL.equalsIgnoreCase(dto.getUserLabel()) ? 1 : 0;
        int dispreferredLabel = preferredLabel == 1 ? 0 : 1;

        Map<String, Object> feedbackItem = new HashMap<>();
        feedbackItem.put("text", dto.getEmailBody());
        feedbackItem.put("preferred_label", preferredLabel);
        feedbackItem.put("dispreferred_label", dispreferredLabel);

        Map<String, Object> payload = new HashMap<>();
        payload.put("user_id", dto.getUserId().toString());
        payload.put("preferences", Collections.singletonMap(
                "feedback", Collections.singletonList(feedbackItem)));

        return payload;
    }

    private void validateFeedbackInput(PeoplePreferenceDTO dto) {
        if (dto.getUserLabel() == null ||
                (!dto.getUserLabel().equalsIgnoreCase(SPAM_LABEL) &&
                        !dto.getUserLabel().equalsIgnoreCase(HAM_LABEL))) {
            throw new IllegalArgumentException(
                    "User label must be either 'spam' or 'ham'");
        }

        if (dto.getEmailBody() == null || dto.getEmailBody().isBlank()) {
            throw new IllegalArgumentException("Email body cannot be empty");
        }
    }
}