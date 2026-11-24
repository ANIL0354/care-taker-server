package com.careride.booking.dto;

import com.careride.booking.entity.Direction;
import jakarta.validation.constraints.NotNull;

public record RideRequest(@NotNull Direction direction, @NotNull Long slotId) {
}
