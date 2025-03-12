package com.tsix_hack.spam_ai_detection.controller;

import com.tsix_hack.spam_ai_detection.entities.Customer;
import com.tsix_hack.spam_ai_detection.service.UserService;
import com.tsix_hack.spam_ai_detection.service.WhatsApp;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/user")
@AllArgsConstructor
public class UserController {
    private UserService userService ;
    private WhatsApp whatsApp ;

    @PostMapping("/save")
    private Customer save(@RequestBody Customer user) {
        return userService.save(user);
    }

    @GetMapping("/getAll")
    List<Customer> getAll() {
        return userService.findAll();
    }

    @GetMapping("/getById/{id}")
    Optional<Customer> getById(@PathVariable UUID id) {
        return userService.findById(id);
    }

    @PostMapping("/verification")
    public void sendMsg (@RequestParam String to ) throws IOException {
        whatsApp.sendMsg(to , "hello from java api");
    }

}
