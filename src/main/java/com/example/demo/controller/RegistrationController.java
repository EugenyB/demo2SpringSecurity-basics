package com.example.demo.controller;

import com.example.demo.model.Account;
import com.example.demo.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequiredArgsConstructor
public class RegistrationController {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public String register(@RequestParam String username,
                           @RequestParam String password) {
        if (accountRepository.findByUsername(username).isPresent()) {
            return "Username already exists";
        }
        accountRepository.save(new Account(null, username,
                passwordEncoder.encode(password), "USER"));
        return "User registered successfully";
    }
}
