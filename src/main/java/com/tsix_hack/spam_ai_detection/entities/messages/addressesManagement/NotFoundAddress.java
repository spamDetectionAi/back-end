package com.tsix_hack.spam_ai_detection.entities.messages.addressesManagement;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class NotFoundAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id ;
    @Column(unique = true)
    String address ;

    public NotFoundAddress(String address) {
        this.address = address ;
    }
}
