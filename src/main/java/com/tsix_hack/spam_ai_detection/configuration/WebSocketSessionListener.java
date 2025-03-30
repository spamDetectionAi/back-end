package com.tsix_hack.spam_ai_detection.configuration;

import com.tsix_hack.spam_ai_detection.entities.MessageRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class WebSocketSessionListener {
    private final SimpMessagingTemplate messagingTemplate;
    private static final Map<String , String>  sessions = new ConcurrentHashMap<>();

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectEvent event) {
        Map<String, Object> sessionAttributes = (Map<String, Object>) event.getMessage().getHeaders().get("simpSessionAttributes");

        if (sessionAttributes != null && sessionAttributes.containsKey("userId")) {
            String userId = (String) sessionAttributes.get("userId");
            String sessionId = event.getMessage().getHeaders().get("simpSessionId").toString();
            sessions.put(userId, sessionId);
            System.out.println("🔗 Connexion WebSocket : " + userId + " -> " + sessionId);
            System.out.println(sessions);
        } else {
            System.out.println("⚠️ Impossible de récupérer l'UUID de l'utilisateur");
        }
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        String sessionId = event.getSessionId();
        String userIdToRemove = null;

        // Rechercher le userId correspondant à la sessionId
        for (Map.Entry<String, String> entry : sessions.entrySet()) {
            if (entry.getValue().equals(sessionId)) {
                userIdToRemove = entry.getKey();
                break;
            }
        }

        if (userIdToRemove != null) {
            sessions.remove(userIdToRemove);
            System.out.println("❌ Déconnexion WebSocket : " + userIdToRemove + " (session " + sessionId + ")");
        } else {
            System.out.println("⚠️ Session non trouvée : " + sessionId);
        }
    }

    public  MessageRequest sendMessageToUser(MessageRequest messageRequest) {
        String userId = messageRequest.getRecipients().toString() ;
        String sessionId = sessions.get(userId);
        if (sessionId != null) {
            try {
                System.out.println("🛠️ Tentative d'envoi au userId : " + userId + " (sessionId : " + sessions.get(userId) + ")");
                messagingTemplate.convertAndSendToUser(userId, "/queue/messages", messageRequest);
                System.out.println("📩 Message sent to " + userId + " : " + messageRequest.getRecipients() + " -> " + sessionId);
                return messageRequest;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            //messagingTemplate.convertAndSend("/user/" + userId + "/queue/messages", messageRequest);

        } else {
            System.out.println("⚠️ user " + userId+" is not connected");
        }
        return null;
    }

}
