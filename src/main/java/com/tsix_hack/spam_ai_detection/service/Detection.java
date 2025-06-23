package com.tsix_hack.spam_ai_detection.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


import java.util.Map;

@Service
public class Detection {
    private final WebClient webClient ;
    private final String baseUrl = System.getenv("DETECTION_URL") ;

    public Detection(WebClient.Builder builder){
        this.webClient = builder.baseUrl(baseUrl).build();
    }

    public boolean callDetector(String text){
        Map<String , String> body = Map.of(
                "use_id", "1234", // valeur fictive mais non vide
                "text", text
        );

        try {
            Boolean result = webClient.post()
                    .uri("/predict")
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(DetectionResponse.class)
                    .map(DetectionResponse::isSpam)
                    .block();

            return Boolean.TRUE.equals(result);
        } catch (Exception e) {
            System.err.println("Erreur appel détecteur : " + e.getMessage());
            return false;
        }
    }

    private record DetectionResponse(boolean isSpam) {}
}
