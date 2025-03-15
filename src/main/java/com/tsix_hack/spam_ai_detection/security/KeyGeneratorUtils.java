package com.tsix_hack.spam_ai_detection.security;

import org.springframework.stereotype.Component;

import java.security.KeyPair;
import java.security.KeyPairGenerator;

public class KeyGeneratorUtils {
    public KeyGeneratorUtils(){}

    public static KeyPair generateRsaKeyPair(){
        KeyPair keyPair;
        try{
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            keyPair = keyPairGenerator.generateKeyPair() ;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        return keyPair ;
    }
}
