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
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;


@Service
@AllArgsConstructor
public class AccountService {
    AccountRepository accountRepository;
    WhatsAppService whatsAppService;
    RandomGenerator randomGenerator;
    private PhoneVerificationRepository phoneVerificationRepository ;

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
            UUID uuid = phoneVerification.get().getUuid(); ;
            return uuid ;
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

    @Transactional
    public ResponseEntity<String> save (Account account , UUID uuid) {
        if (emailCheck(account.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(account.getEmail() + " is already in use") ;

        } else if (!identification(account.getPhoneNumber(), uuid)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("your verification code "+uuid.toString()+" is invalid") ;
        }
        phoneVerificationRepository.deletePhoneNumberVerificationByPhoneNumber(account.getPhoneNumber());
        accountRepository.save(account);
        return ResponseEntity.status(HttpStatus.CREATED).body("account has been saved");

    }


}
