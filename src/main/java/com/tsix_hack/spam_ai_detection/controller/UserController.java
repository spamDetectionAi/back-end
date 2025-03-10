package com.tsix_hack.spam_ai_detection.controller;

import com.tsix_hack.spam_ai_detection.entities.Customer;
import com.tsix_hack.spam_ai_detection.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService ;

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

}
