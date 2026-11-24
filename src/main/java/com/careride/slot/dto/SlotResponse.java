package com.careride.slot.dto;

import java.time.LocalDateTime;

public record SlotResponse(Long id, LocalDateTime startTime, LocalDateTime endTime, int availableVehicles) {
}
