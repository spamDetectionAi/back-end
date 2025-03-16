package com.tsix_hack.spam_ai_detection.service;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.tsix_hack.spam_ai_detection.entities.Account;
import com.tsix_hack.spam_ai_detection.repositories.AccountRepository;
import com.tsix_hack.spam_ai_detection.security.Jwks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jose.jws.JwsAlgorithms;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TokenService {
    private  final JwtEncoder jwtEncoder;
    public TokenService(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }
    @Autowired
    AccountRepository accountRepository;

    public String generateToken(Authentication authentication) {
        Optional<Account> account = accountRepository.findAccountByEmail(authentication.getName()) ;
        Instant now = Instant.now();
         String scope = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" ")); ;
        JwtClaimsSet claimsSet = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(1 , ChronoUnit.HALF_DAYS))
                .subject(account.get().getId().toString())
                .claim("scope" , scope)
                .build();


        JwsHeader jwsHeader = JwsHeader.with(() -> "RS256").build();

        return this.jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader , claimsSet)).getTokenValue();
    }
}
