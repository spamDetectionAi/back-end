package com.tsix_hack.spam_ai_detection.entities.messages.addressesManagement;

import com.tsix_hack.spam_ai_detection.repositories.AccountRepository;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;
@Getter
public class AddressClassification {
    private final Set<String> foreignAddresses ;
    private final Set<String> localAddresses ;
    private final Set<String> notFoundAddresses ;
    private final AccountRepository accountRepository;

    public AddressClassification(Set<String> addresses, AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
        Set<String> foreignAddresses = new HashSet<>();
        Set<String> localAddresses = new HashSet<>();
        Set<String> notFoundAddresses = new HashSet<>();

        for (String address : addresses) {
            if (address.toLowerCase().substring(address.indexOf("@") + 1).equals("maily.tech")) {
                if (accountRepository.findAccountByEmail(address).isPresent()) {
                    localAddresses.add(address.toLowerCase());
                } else {
                    notFoundAddresses.add(address);
                }
            } else {
                foreignAddresses.add(address);
            }
        }

        this.foreignAddresses = foreignAddresses;
        this.localAddresses = localAddresses;
        this.notFoundAddresses = notFoundAddresses;
    }
    


}
