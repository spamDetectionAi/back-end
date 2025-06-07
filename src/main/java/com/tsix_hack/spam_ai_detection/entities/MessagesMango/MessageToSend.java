package com.tsix_hack.spam_ai_detection.entities.MessagesMango;

import java.time.Instant;

public record MessageToSend (String sender , String object , String body , Instant sendDateTime) {
}
