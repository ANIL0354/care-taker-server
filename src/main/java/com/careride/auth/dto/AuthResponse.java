package com.careride.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Authentication response containing JWT token")
public record AuthResponse(
        @Schema(description = "JWT Bearer token for authenticated requests", example = "eyJhbGciOiJIUzI1NiJ9...")
        String token,
        @Schema(description = "Authenticated user's display name", example = "Rahul Verma")
        String name,
        @Schema(description = "Authenticated user's role", example = "CARETAKER")
        String role
) {
}
