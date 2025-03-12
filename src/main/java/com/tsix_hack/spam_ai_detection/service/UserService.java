package com.tsix_hack.spam_ai_detection.service;

import com.tsix_hack.spam_ai_detection.entities.Customer;
import com.tsix_hack.spam_ai_detection.repositories.UserInterface;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService {

    private UserInterface userInterface;

    public Customer save(Customer user) {
        userInterface.save(user);
        return user ;
    }


    public List<Customer> findAll() {
        return userInterface.findAll() ;
    }

    public Optional<Customer> findById(UUID id) {
        return userInterface.findById(id);
    }
}
