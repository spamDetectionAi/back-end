package com.tsix_hack.spam_ai_detection.controller;

import com.tsix_hack.spam_ai_detection.utils.WhatsAppService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/account")
@AllArgsConstructor
public class UserController {
    private WhatsAppService whatsAppService;


    @PostMapping("/verification")
    public void sendMsg (@RequestParam String to ) throws IOException {
        whatsAppService.sendMsg(to , "hello from java api");
    }

}
