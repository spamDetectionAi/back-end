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

    public Mono<Boolean> callDetector(String text){
        Map<String , String> body = Map.of(
                "use_id" , "" ,
                "text" , text
        ) ;

        return webClient.post()
                .uri("/predict")
                .bodyValue(body)
                .retrieve()
                .bodyToMono(DetectionResponse.class)
                .map(DetectionResponse::isSpam);
    }

    private record DetectionResponse(boolean isSpam) {}
}
