package com.tsix_hack.spam_ai_detection.service;
import com.tsix_hack.spam_ai_detection.entities.account.accountForm.Account;
import com.tsix_hack.spam_ai_detection.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TokenService {
    private  final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;
    public TokenService(JwtEncoder jwtEncoder, JwtDecoder jwtDecoder) {
        this.jwtEncoder = jwtEncoder;
        this.jwtDecoder = jwtDecoder;
    }
    @Autowired
    AccountRepository accountRepository;

    public String generateToken(Authentication authentication) {
        Optional<Account> account = accountRepository.findAccountByEmail(authentication.getName()) ;
        Instant now = Instant.now();
         String scope = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));
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

    public String uuidDecoded(String token) {
        try {
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            return jwtDecoder.decode(token).getSubject();
        } catch (JwtException e) {
            return "Error : " + e.getMessage();
        }
    }

    public String decodeInParam (String token) {
        return jwtDecoder.decode(token).getSubject();
    }
}
