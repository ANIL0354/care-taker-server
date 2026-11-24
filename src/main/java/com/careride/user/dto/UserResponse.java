package com.careride.user.dto;

import com.careride.user.entity.Role;

public record UserResponse(Long id, String name, String email, String phone, Role role) {
}
