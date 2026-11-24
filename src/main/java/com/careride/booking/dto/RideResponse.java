package com.careride.booking.dto;

import com.careride.booking.entity.Direction;

import java.time.LocalDateTime;

public record RideResponse(Direction direction, LocalDateTime startTime, LocalDateTime endTime) {
}
