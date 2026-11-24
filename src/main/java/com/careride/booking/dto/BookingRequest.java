package com.careride.booking.dto;

import com.careride.booking.entity.MobilityNeed;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.List;

public record BookingRequest(
        @NotNull Long hospitalId,
        @NotBlank String patientName,
        @NotBlank @Pattern(regexp = "\\d{10}", message = "Patient phone must be a 10-digit number") String patientPhone,
        @NotBlank String homeAddress,
        @NotNull MobilityNeed mobilityNeed,
        String notes,
        @NotNull @Size(min = 1, max = 2) List<@Valid RideRequest> rides
) {
}
