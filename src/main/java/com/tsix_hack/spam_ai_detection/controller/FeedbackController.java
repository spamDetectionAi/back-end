package com.tsix_hack.spam_ai_detection.controller;// FeedbackController.java
import com.tsix_hack.spam_ai_detection.entities.preference.PeoplePreferenceDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.tsix_hack.spam_ai_detection.service.FeedbackService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/feedback")
public class FeedbackController {

    private final FeedbackService feedbackService;

    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }
    @PostMapping("/feedback")
    public ResponseEntity<?> handleUserFeedback(@RequestBody PeoplePreferenceDTO preference) {
        try {

            feedbackService.saveFeedbackToDB(preference);


            feedbackService.sendFeedbackToFastAPI(preference);


            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Feedback saved and sent to model service"
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "error",
                    "message", "Could not process feedback",
                    "details", e.getMessage()
            ));
        }
    }

}
