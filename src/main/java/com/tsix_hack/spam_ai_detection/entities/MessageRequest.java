package com.tsix_hack.spam_ai_detection.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class MessageRequest {
    private UUID recipients ;
    private String content ;
    private String subject ;
}
