package com.tsix_hack.spam_ai_detection.repositories.MessagesRepositories;

import com.tsix_hack.spam_ai_detection.entities.messages.addressesManagement.NotFoundAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface NotFoundAddressesRepository extends JpaRepository<NotFoundAddress , Long> {

    @Query("SELECT n FROM ForeignAddresses n WHERE n.address = :address")
    Optional<NotFoundAddress> findIdByAddress(String address);

}
