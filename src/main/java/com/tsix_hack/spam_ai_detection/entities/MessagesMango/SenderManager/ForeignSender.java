package com.tsix_hack.spam_ai_detection.entities.MessagesMango.SenderManager;

import software.amazon.awssdk.services.s3.endpoints.internal.Value;

public final record ForeignSender(String email) implements Sender {
}
