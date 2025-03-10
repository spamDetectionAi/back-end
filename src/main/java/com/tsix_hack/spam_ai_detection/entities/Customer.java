package com.tsix_hack.spam_ai_detection.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigInteger;
import java.sql.Date;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "customer")
public class Customer implements Serializable {
    @Id
    private UUID id ;
    private String firstName;
    private String lastName;
    private Date birthDate ;
    private String emaail ;
    private String password ;
    private BigInteger phoneNumber ;
}
