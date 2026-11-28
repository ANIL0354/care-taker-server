package com.careride.user.dto;

import com.careride.user.entity.Role;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "User details")
public record UserResponse(
        @Schema(description = "User ID", example = "1")
        Long id,
        @Schema(description = "User name", example = "Rahul Verma")
        String name,
        @Schema(description = "Email address", example = "rahul@example.com")
        String email,
        @Schema(description = "Phone number", example = "9876543210")
        String phone,
        @Schema(description = "User role", example = "CARETAKER")
        Role role
) {
}
