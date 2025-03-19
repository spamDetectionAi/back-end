package com.tsix_hack.spam_ai_detection.configuration;

import lombok.AllArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.oauth2.jwt.JwtDecoder;

import java.security.Principal;

@AllArgsConstructor
public class JwtChannelInterceptor implements ChannelInterceptor {

    private JwtDecoder jwtDecoder;

    @Override
    public Message<?> preSend(Message<?> message , MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if(StompCommand.CONNECT.equals(accessor.getCommand())) {
            String token = accessor.getFirstNativeHeader("Authorization");
            if(token != null && token.startsWith("Bearer ")) {
                String userId = jwtDecoder.decode(token.substring(7) ).toString();
                accessor.setUser(new Principal() {
                    @Override
                    public String getName() {
                        return userId;
                    }
                });
            }
        }
        return message;
    }

}
