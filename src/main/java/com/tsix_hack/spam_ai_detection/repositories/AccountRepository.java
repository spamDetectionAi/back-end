package com.tsix_hack.spam_ai_detection.repositories;

import com.tsix_hack.spam_ai_detection.entities.account.accountForm.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<Account, UUID> {
    Optional<Account> findByPhoneNumber(String phoneNumber);
    Optional<Account> findAccountByEmail(String email);
    Account findAccountById(UUID id);
    
}
