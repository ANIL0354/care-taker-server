package com.careride.booking.dto;

import com.careride.booking.entity.MobilityNeed;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.List;

@Schema(description = "Booking request for one-way or round-trip patient transport")
public record BookingRequest(
        @Schema(description = "Target hospital ID", example = "1")
        @NotNull Long hospitalId,
        @Schema(description = "Patient name", example = "Asha Verma")
        @NotBlank String patientName,
        @Schema(description = "Patient 10-digit phone number", example = "9876543211")
        @NotBlank @Pattern(regexp = "\\d{10}", message = "Patient phone must be a 10-digit number") String patientPhone,
        @Schema(description = "Patient pickup/drop home address", example = "12 Main Street, Bengaluru")
        @NotBlank String homeAddress,
        @Schema(description = "Patient mobility requirement", example = "WALKING")
        @NotNull MobilityNeed mobilityNeed,
        @Schema(description = "Optional booking notes", example = "Need wheelchair assistance")
        String notes,
        @Schema(description = "Ride legs: 1 for one-way, 2 for round trip. Return slot must start after pickup slot ends.")
        @NotNull @Size(min = 1, max = 2) List<@Valid RideRequest> rides
) {
}
