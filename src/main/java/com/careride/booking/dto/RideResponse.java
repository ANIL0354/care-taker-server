package com.careride.booking.dto;

import com.careride.booking.entity.Direction;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Booked ride leg")
public record RideResponse(
        @Schema(description = "Ride direction", example = "HOME_TO_HOSPITAL")
        Direction direction,
        @Schema(description = "Ride start time", example = "2026-11-28T08:00:00")
        LocalDateTime startTime,
        @Schema(description = "Ride end time", example = "2026-11-28T08:30:00")
        LocalDateTime endTime
) {
}
