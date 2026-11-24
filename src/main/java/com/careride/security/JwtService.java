package com.careride.security;

import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {

    String generateToken(UserDetails userDetails);

    String extractEmail(String token);

    boolean isValid(String token);
}
