package com.tsix_hack.spam_ai_detection.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Ping {
    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }
}
