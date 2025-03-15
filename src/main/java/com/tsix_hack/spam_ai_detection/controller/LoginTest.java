package com.tsix_hack.spam_ai_detection.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginTest {
    @Autowired
    private JwtDecoder jwtDecoder ;

    @GetMapping("/auth")
    public String ping (@RequestHeader("Authorization") String token){
        try{
            return jwtDecoder.decode(token).getSubject() ;
        }catch (JwtException e){
            return e.getMessage() ;
        }

    }

}
