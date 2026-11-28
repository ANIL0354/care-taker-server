package com.careride.booking.dto;

import com.careride.booking.entity.Direction;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Single ride leg in a booking")
public record RideRequest(
        @Schema(description = "Ride direction", example = "HOME_TO_HOSPITAL")
        @NotNull Direction direction,
        @Schema(description = "Pickup slot ID", example = "10")
        @NotNull Long slotId
) {
}
