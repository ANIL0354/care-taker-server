package com.careride.hospital.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Hospital details")
public record HospitalResponse(
        @Schema(description = "Hospital ID", example = "1")
        Long id,
        @Schema(description = "Hospital name", example = "City General Hospital")
        String name,
        @Schema(description = "Hospital address", example = "123 Main Street")
        String address,
        @Schema(description = "City", example = "Bengaluru")
        String city
) {
}
