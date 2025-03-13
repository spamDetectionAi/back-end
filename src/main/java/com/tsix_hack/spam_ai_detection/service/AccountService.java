package com.tsix_hack.spam_ai_detection.service;

import com.tsix_hack.spam_ai_detection.entities.Account;
import com.tsix_hack.spam_ai_detection.repositories.AccountRepository;
import com.tsix_hack.spam_ai_detection.utils.RandomGenerator;
import com.tsix_hack.spam_ai_detection.utils.RedisService;
import com.tsix_hack.spam_ai_detection.utils.WhatsAppService;
import kotlin.jvm.Throws;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class AccountService {
    AccountRepository accountRepository;
    RedisService redisService;
    WhatsAppService whatsAppService;
    RandomGenerator randomGenerator;

    public String initiateVerification (String phoneNumber) throws IOException {
        Optional<Account> account = accountRepository.findByPhoneNumber(phoneNumber);
        if (account.isPresent()) {
            return "this phone number is already in use" ;
        }
        else {
            whatsAppService.sendMsg(phoneNumber ,
                    randomGenerator.randomInt().toString() );
            redisService.setValue(phoneNumber ,
                    randomGenerator.getRandomUUID().toString() ,
                    5 ,
                    TimeUnit.MINUTES);
            return "your code verification is sent on " + phoneNumber ;
        }
    }

    /*public String validateCode(String code) {

    }*/
}
