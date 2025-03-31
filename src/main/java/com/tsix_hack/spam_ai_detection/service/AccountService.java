package com.tsix_hack.spam_ai_detection.service;

import com.tsix_hack.spam_ai_detection.entities.Account;
import com.tsix_hack.spam_ai_detection.entities.PhoneNumberVerification;
import com.tsix_hack.spam_ai_detection.repositories.AccountRepository;
import com.tsix_hack.spam_ai_detection.repositories.PhoneVerificationRepository;
import com.tsix_hack.spam_ai_detection.utils.RandomGenerator;
import com.tsix_hack.spam_ai_detection.utils.WhatsAppService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;



@Service
@AllArgsConstructor
public class AccountService {
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private AccountRepository accountRepository;
    private WhatsAppService whatsAppService;
    private RandomGenerator randomGenerator;
    private PhoneVerificationRepository phoneVerificationRepository ;
    private TokenService tokenService;

    public String initiateVerification (String phoneNumber) throws IOException {
        Optional<Account> account = accountRepository.findByPhoneNumber(phoneNumber);
        if (account.isPresent()) {
            return "this phone number is already in use" ;
        }
        else {

            String code = randomGenerator.randomInt().toString() ;
            UUID uuid = randomGenerator.getRandomUUID();
            phoneVerificationRepository.save(new PhoneNumberVerification(null ,phoneNumber, code, uuid));
            whatsAppService.sendMsg(phoneNumber ,
                    "your verification code is "+code );

            return "your code verification has been sent on " + phoneNumber ;
        }
    }

    public UUID validateCode(String code , String phoneNumber) {
        Optional<PhoneNumberVerification> phoneVerification = phoneVerificationRepository.findPhoneNumberVerificationByPhoneNumber(phoneNumber);
       if (phoneVerification.get()
                .getVerificationCode()
                .equals(code)) {
           return phoneVerification.get().getUuid();
        }
        else
            return null ;
    }

    private boolean identification (String phoneNumber , UUID uuid){
        return phoneVerificationRepository.
                findPhoneNumberVerificationByPhoneNumber(phoneNumber)
                .get()
                .getUuid()
                .equals(uuid);
    }

    private boolean emailCheck (String email) {
        return accountRepository.findAccountByEmail(email).isPresent();
    }

    private ResponseEntity<String> savingAction (Account account){
        //phoneVerificationRepository.deletePhoneNumberVerificationByPhoneNumber(account.getPhoneNumber())
        account.setAccountPermission(2);
        account.setAccountPassword(passwordEncoder.encode(account.getPassword())) ;
        Account account1 = accountRepository.save(account);
        Authentication authentication = new UsernamePasswordAuthenticationToken(account1.getEmail(), account1.getPassword());
        String token = tokenService.generateToken(authentication);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(token);
    }

    @Transactional
    public ResponseEntity<String> save (Account account , UUID uuid) {
        if (emailCheck(account.getEmail()))
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(account.getEmail() + " is already in use") ;

        /*else if (!identification(account.getPhoneNumber(), uuid))
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("your verification code "+uuid.toString()+" is invalid") ;*/

        return savingAction(account);
    }

    public static void main(String[] args) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        System.out.println(passwordEncoder.encode("securePassword123"));
        System.out.println(passwordEncoder.matches("securePassword123", "$2a$10$mV9fwPYggAs54xyp9s.SlOcBNdZa1L5IVz5IBb7loNDZqcBuCr9c2"));
    }

    public Set<Account> findAllById(Set<UUID> ids) {
        Set<Account> accounts = new HashSet<>();
        for (UUID id : ids){
            accounts.add(accountRepository.findAccountById(id));
        }
        return accounts;
    }

}
