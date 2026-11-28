package com.careride.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Login request")
public record LoginRequest(
        @Schema(description = "Registered email address", example = "admin@careride.com")
        @NotBlank @Email String email,
        @Schema(description = "Account password", example = "Admin@123")
        @NotBlank String password
) {
}
