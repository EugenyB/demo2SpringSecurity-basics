package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_USER = "USER";
    public static final String ROLE_GUEST = "GUEST";

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/login", "/access-denied", "/css/**").permitAll()
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

    /**
     * Three demo users stored in memory. Passwords are encoded at startup.
     */
    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder encoder) {
        UserDetails admin = User.withUsername("admin")
            .password(encoder.encode("admin123"))
            .roles(ROLE_ADMIN)
            .build();

        UserDetails user = User.withUsername("user")
            .password(encoder.encode("user123"))
            .roles(ROLE_USER)
            .build();

        UserDetails guest = User.withUsername("guest")
            .password(encoder.encode("guest123"))
            .roles(ROLE_GUEST)
            .build();

        return new InMemoryUserDetailsManager(admin, user, guest);
    }
}
