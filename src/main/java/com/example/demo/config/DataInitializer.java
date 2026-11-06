package com.example.demo.config;

import com.example.demo.model.Account;
import com.example.demo.repository.AccountRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final AccountRepository accountRepository;

    private final PasswordEncoder passwordEncoder;

    @Value("${password.admin}")
    private String pwdAdmin;

    @Value("${password.user}")
    private String pwdUser;

    @Value("${password.guest}")
    private String pwdGuest;

    @Override
    public void run(String @NonNull ... args) throws Exception {
        // Only initialize if no accounts exist
        if (accountRepository.count() == 0) {
            accountRepository.save(new Account(null, "admin", passwordEncoder.encode(pwdAdmin), "ADMIN"));
            accountRepository.save(new Account(null, "user", passwordEncoder.encode(pwdUser), "USER"));
            accountRepository.save(new Account(null, "guest", passwordEncoder.encode(pwdGuest), "GUEST"));
        }
    }
}
