package com.tsix_hack.spam_ai_detection.configuration;

import com.tsix_hack.spam_ai_detection.service.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import java.util.Map;

@AllArgsConstructor
@Component
public class JwtChannelInterceptor implements HandshakeInterceptor {

    private TokenService jwtDecoder;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request,
                                   ServerHttpResponse response,
                                   WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) throws Exception {
        if (request instanceof ServletServerHttpRequest){
           HttpServletRequest servletRequest = ((ServletServerHttpRequest) request)
                    .getServletRequest();
            String token = servletRequest.getParameter("token");
            String userId = jwtDecoder.decodeInParam(token);
            if (userId != null) {
                attributes.put("userId", userId);
                response.setStatusCode(HttpStatus.OK);
                return true;
            }
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return false;
        }
        return false;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request,
                               ServerHttpResponse response,
                               WebSocketHandler wsHandler,
                               Exception exception) {

    }
}
