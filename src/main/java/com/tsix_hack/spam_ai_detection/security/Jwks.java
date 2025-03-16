package com.tsix_hack.spam_ai_detection.security;

import java.security.KeyPair;
import com.nimbusds.jose.jwk.RSAKey;
import org.springframework.stereotype.Component;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;

public class Jwks {
    private Jwks(){}

    public static RSAKey generateRsa(){
        KeyPair keyPair = KeyGeneratorUtils.generateRsaKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        return new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyID(UUID.randomUUID().toString())
                .build();
    }
}
