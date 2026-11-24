package com.careride.auth.dto;

public record AuthResponse(String token, String name, String role) {
}
