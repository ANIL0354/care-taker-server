package com.careride.auth.service;

import com.careride.auth.dto.AuthResponse;
import com.careride.auth.dto.LoginRequest;
import com.careride.auth.dto.SignupRequest;
import com.careride.auth.mapper.AuthMapper;
import com.careride.auth.repository.AuthRepository;
import com.careride.security.JwtService;
import com.careride.user.entity.Role;
import com.careride.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthMapper authMapper;

    @Override
    @Transactional
    public AuthResponse signup(SignupRequest request) {
        String email = normalize(request.email());
        if (authRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email is already registered");
        }
        User user = authRepository.save(User.builder()
                .name(request.name().trim())
                .email(email)
                .phone(request.phone().trim())
                .password(passwordEncoder.encode(request.password()))
                .role(Role.CARETAKER)
                .build());
        return withToken(user);
    }

    @Override
    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        User user = authRepository.findByEmail(normalize(request.email()))
                .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));
        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new BadCredentialsException("Invalid email or password");
        }
        return withToken(user);
    }

    private AuthResponse withToken(User user) {
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + user.getRole().name());
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                user.getEmail(), user.getPassword(), List.of(authority));
        AuthResponse response = authMapper.toResponse(user);
        return new AuthResponse(jwtService.generateToken(userDetails), response.name(), response.role());
    }

    private String normalize(String email) {
        return email == null ? null : email.trim().toLowerCase(Locale.ROOT);
    }
}
