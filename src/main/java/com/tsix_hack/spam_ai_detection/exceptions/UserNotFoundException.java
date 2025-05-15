package com.tsix_hack.spam_ai_detection.exceptions;

import java.util.UUID;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(UUID userId) {
        super("User not found with ID: " + userId);
    }

}