package com.example.demo.model;

import java.util.List;

/**
 * View model describing the authenticated user (exposed to Thymeleaf templates).
 */
public record CurrentUser(String username, List<String> roles) {

    public boolean hasRole(String role) {
        return roles.contains(role);
    }

    public boolean hasAnyRole(String... candidates) {
        for (String candidate : candidates) {
            if (roles.contains(candidate)) {
                return true;
            }
        }
        return false;
    }
}
