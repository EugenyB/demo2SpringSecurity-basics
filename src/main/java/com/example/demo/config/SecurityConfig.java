package com.example.demo.config;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {

    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_USER = "USER";
    public static final String ROLE_GUEST = "GUEST";

    private UserDetailsService userDetailsService;

    @Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
            .csrf(csrf -> csrf
                    .ignoringRequestMatchers("/register"))   // ignore csrf for registering
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/", "/login", "/register", "/access-denied", "/css/**").permitAll()
                    .requestMatchers("/admin/**").hasRole(ROLE_ADMIN)
                    .requestMatchers("/user/**").hasAnyRole(ROLE_USER, ROLE_ADMIN)
                    .requestMatchers("/guest/**").hasAnyRole(ROLE_GUEST, ROLE_USER, ROLE_ADMIN)
                    .anyRequest().authenticated())
            .formLogin(form -> form
                    .loginPage("/login")
                    .permitAll())
            .logout(logout -> logout
                    .logoutSuccessUrl("/login?logout")
                    .permitAll())
            .exceptionHandling(ex -> ex
                    .accessDeniedPage("/access-denied"));

    return http.build();
}

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
