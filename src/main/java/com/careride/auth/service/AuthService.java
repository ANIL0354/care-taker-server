package com.careride.auth.service;

import com.careride.auth.dto.AuthResponse;
import com.careride.auth.dto.LoginRequest;
import com.careride.auth.dto.SignupRequest;

public interface AuthService {

    AuthResponse signup(SignupRequest request);

    AuthResponse login(LoginRequest request);
}
