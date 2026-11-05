package com.example.demo.model;

/**
 * Simple read-only description of a demo account (shown on the admin page).
 */
public record UserInfo(String username, String role, String description) {
}
