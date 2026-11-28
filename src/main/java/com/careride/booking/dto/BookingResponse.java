package com.careride.booking.dto;

import com.careride.booking.entity.BookingStatus;
import com.careride.booking.entity.MobilityNeed;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Booking confirmation response")
public record BookingResponse(
        @Schema(description = "Booking ID", example = "1")
        Long id,
        @Schema(description = "Hospital name", example = "City General Hospital")
        String hospitalName,
        @Schema(description = "Patient name", example = "Asha Verma")
        String patientName,
        @Schema(description = "Patient phone number", example = "9876543211")
        String patientPhone,
        @Schema(description = "Home address", example = "12 Main Street, Bengaluru")
        String homeAddress,
        @Schema(description = "Mobility requirement", example = "WALKING")
        MobilityNeed mobilityNeed,
        @Schema(description = "Booked ride legs")
        List<RideResponse> rides,
        @Schema(description = "Booking status", example = "CONFIRMED")
        BookingStatus status
) {
}
