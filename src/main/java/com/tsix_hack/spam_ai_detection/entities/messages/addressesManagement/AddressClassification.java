package com.tsix_hack.spam_ai_detection.entities.messages.addressesManagement;

import com.tsix_hack.spam_ai_detection.entities.account.accountForm.Account;
import com.tsix_hack.spam_ai_detection.repositories.AccountRepository;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
public class AddressClassification {
    private final Set<String> foreignAddresses;
    private final Set<String> notFoundAddresses;
    private final AccountRepository accountRepository;
    private final Set<UUID> UUIDs;

    public AddressClassification(Set<String> addresses, AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
        Set<String> foreignAddresses = new HashSet<>();
        Set<String> notFoundAddresses = new HashSet<>();
        Set<UUID> UUIDs = new HashSet<>();

        for (String address : addresses) {
            if (address.toLowerCase().substring(address.indexOf("@") + 1).equals("maily.tech")) {
                Account account = accountRepository.findAccountByEmail(address).orElse(null);
                if (account != null) {
                    UUIDs.add(account.getId());
                } else {
                    notFoundAddresses.add(address);
                }
            } else {
                foreignAddresses.add(address);
            }
        }

        this.foreignAddresses = foreignAddresses;
        this.notFoundAddresses = notFoundAddresses;
        this.UUIDs = UUIDs;
    }


}
