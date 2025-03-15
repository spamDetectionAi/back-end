package com.tsix_hack.spam_ai_detection.security;

import com.tsix_hack.spam_ai_detection.entities.Account;
import com.tsix_hack.spam_ai_detection.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@Service

public class UserDetailImpl implements UserDetailsService {
    @Autowired
    AccountRepository accountRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
         Optional<Account> account = accountRepository.findAccountByEmail(email) ;
        if (!account.isPresent())
            throw new UsernameNotFoundException("account not found");

        return User.builder()
                .username(account.get().getEmail())
                .password(account.get().getPassword())
                .authorities(new SimpleGrantedAuthority(account.get().getPermission().toString()))
                .build();
    }
}
