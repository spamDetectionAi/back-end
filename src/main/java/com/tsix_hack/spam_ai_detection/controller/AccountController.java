package com.tsix_hack.spam_ai_detection.controller;

import com.tsix_hack.spam_ai_detection.entities.Account;
import com.tsix_hack.spam_ai_detection.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("api/account")
@AllArgsConstructor
public class AccountController {
    private AccountService accountService;

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
