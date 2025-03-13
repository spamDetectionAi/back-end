package com.tsix_hack.spam_ai_detection.utils;

import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.UUID;

@Service
public class RandomGenerator {
    public UUID getRandomUUID() {
        return UUID.randomUUID();
    }

    public Integer randomInt(){
        Random random = new Random();
        return random.nextInt(1000 , 9999) ;
    }
}
