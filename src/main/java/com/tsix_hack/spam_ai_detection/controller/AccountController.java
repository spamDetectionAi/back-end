package com.tsix_hack.spam_ai_detection.controller;

import com.tsix_hack.spam_ai_detection.entities.Account;
import com.tsix_hack.spam_ai_detection.entities.SignInRequest;
import com.tsix_hack.spam_ai_detection.service.AccountService;
import com.tsix_hack.spam_ai_detection.service.TokenService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("api/account")
@AllArgsConstructor
public class AccountController {
    private AccountService accountService;
    private TokenService tokenService;
    @Autowired
    private final AuthenticationManager authenticationManager;

    @PostMapping("/signIn")
    public ResponseEntity<String> signIn(@RequestBody SignInRequest sign){
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(sign.email(), sign.password())
            );
            String token = tokenService.generateToken(authentication);
            return ResponseEntity.ok().body( token);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/verification/{phoneNumber}")
    public ResponseEntity<String> verificationInitialisation(@PathVariable String phoneNumber) throws IOException {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(accountService
                        .initiateVerification(phoneNumber)) ;
    }

    @PostMapping("/verification/{phoneNumber}/{code}")
    public ResponseEntity<UUID> verification(@PathVariable String phoneNumber , @PathVariable String code)  {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(accountService
                        .validateCode(code , phoneNumber) );
    }

    @PostMapping("/save")
    public ResponseEntity<String> save (@RequestBody Account account ,@RequestHeader("uuid") UUID uuid) {
        return accountService.save(account , uuid) ;
    }
}
