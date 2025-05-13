package com.tsix_hack.spam_ai_detection.repositories;

import com.tsix_hack.spam_ai_detection.entities.account.complementaryElements.PhoneNumberVerification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PhoneVerificationRepository extends JpaRepository<PhoneNumberVerification, Integer> {
    @Query("SELECT p FROM PhoneNumberVerification p WHERE p.phoneNumber = :phoneNumber")
    Optional<PhoneNumberVerification> findPhoneNumberVerificationByPhoneNumber(String phoneNumber);


    void deletePhoneNumberVerificationByPhoneNumber(String phoneNumber);
}
