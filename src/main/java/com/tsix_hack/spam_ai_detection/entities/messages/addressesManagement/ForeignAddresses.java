package com.tsix_hack.spam_ai_detection.entities.messages.addressesManagement;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ForeignAddresses {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;
    @Column(unique = true)
    private String address ;

    public ForeignAddresses(String address) {
        this.address = address;
    }
}
