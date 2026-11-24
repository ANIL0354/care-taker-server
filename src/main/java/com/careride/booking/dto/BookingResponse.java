package com.careride.booking.dto;

import com.careride.booking.entity.BookingStatus;
import com.careride.booking.entity.MobilityNeed;

import java.util.List;

public record BookingResponse(Long id, String hospitalName, String patientName, String patientPhone,
                              String homeAddress, MobilityNeed mobilityNeed, List<RideResponse> rides,
                              BookingStatus status) {
}
