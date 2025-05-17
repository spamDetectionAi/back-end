package com.tsix_hack.spam_ai_detection.controller;

import com.tsix_hack.spam_ai_detection.entities.account.accountForm.Account;
import com.tsix_hack.spam_ai_detection.entities.account.accountForm.AccountDTO;
import com.tsix_hack.spam_ai_detection.entities.account.complementaryElements.SignInRequest;
import com.tsix_hack.spam_ai_detection.entities.messages.messageForm.MessageToSend;
import com.tsix_hack.spam_ai_detection.service.AccountService;
import com.tsix_hack.spam_ai_detection.service.MessageServices;
import com.tsix_hack.spam_ai_detection.service.TokenService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/account")
@AllArgsConstructor
public class AccountController {
    private AccountService accountService;
    private TokenService tokenService;
    private final AuthenticationManager authenticationManager;
    private MessageServices messageServices;

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

    @GetMapping("/info")
    public ResponseEntity<AccountDTO> findById(@RequestHeader("Authorization") String token) {
        return ResponseEntity.status(HttpStatus.OK).body(accountService.findById(token)) ;
    }

    @GetMapping("/messages/{page}")
    public ResponseEntity<Page<MessageToSend>> getAllMessages(@RequestHeader("Authorization") String token , @PathVariable int page) {
        return ResponseEntity.status(HttpStatus.OK).body(messageServices.messagesByReceiver(token , page)) ;
    }

    @GetMapping("/mail/{email}")
    public ResponseEntity<String>mailExistance(@PathVariable String email){
        if (accountService.emailCheck(email))
            return ResponseEntity.status(HttpStatus.CONFLICT).body("this email is already in use") ;
        else
            return ResponseEntity.status(HttpStatus.OK).body("this email is available") ;
    }
}
