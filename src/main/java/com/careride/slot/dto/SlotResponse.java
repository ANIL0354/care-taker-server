package com.careride.slot.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Available pickup slot")
public record SlotResponse(
        @Schema(description = "Slot ID", example = "10")
        Long id,
        @Schema(description = "Slot start time", example = "2026-11-28T08:00:00")
        LocalDateTime startTime,
        @Schema(description = "Slot end time", example = "2026-11-28T08:30:00")
        LocalDateTime endTime,
        @Schema(description = "Remaining vehicle capacity", example = "3")
        int availableVehicles
) {
}
