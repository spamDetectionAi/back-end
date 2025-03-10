package com.tsix_hack.spam_ai_detection.repositories;

import com.tsix_hack.spam_ai_detection.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserInterface extends JpaRepository<Customer, UUID> {
}
