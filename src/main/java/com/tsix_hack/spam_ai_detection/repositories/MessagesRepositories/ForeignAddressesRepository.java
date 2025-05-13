package com.tsix_hack.spam_ai_detection.repositories.MessagesRepositories;

import com.tsix_hack.spam_ai_detection.entities.messages.addressesManagement.ForeignAddresses;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ForeignAddressesRepository extends JpaRepository<ForeignAddresses , Long> {

    @Query(value = "SELECT f FROM ForeignAddresses f where f.address = :address")
    Optional<ForeignAddresses> findByAddress(String address);

}
