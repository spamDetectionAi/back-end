package com.tsix_hack.spam_ai_detection.entities.MessagesMango.SenderManager;

import java.util.List;

public record MessageRequest(String sender, String object, String body, List<String> receivers) {}
