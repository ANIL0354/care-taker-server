package com.careride.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "Signup request for a caretaker account")
public record SignupRequest(
        @Schema(description = "Full name of the caretaker", example = "Rahul Verma")
        @NotBlank String name,
        @Schema(description = "Unique email address", example = "rahul@example.com")
        @NotBlank @Email String email,
        @Schema(description = "10-digit phone number", example = "9876543210")
        @NotBlank @Pattern(regexp = "\\d{10}", message = "Phone must be a 10-digit number") String phone,
        @Schema(description = "Password with at least 8 characters", example = "Password@123", minLength = 8)
        @NotBlank @Size(min = 8) String password
) {
}
