package com.careride.booking.mapper;

import com.careride.booking.dto.BookingResponse;
import com.careride.booking.dto.RideResponse;
import com.careride.booking.entity.Booking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    @Mapping(target = "id", source = "booking.id")
    @Mapping(target = "hospitalName", source = "hospitalName")
    @Mapping(target = "patientName", source = "booking.patientName")
    @Mapping(target = "patientPhone", source = "booking.patientPhone")
    @Mapping(target = "homeAddress", source = "booking.homeAddress")
    @Mapping(target = "mobilityNeed", source = "booking.mobilityNeed")
    @Mapping(target = "rides", source = "rides")
    @Mapping(target = "status", source = "booking.status")
    BookingResponse toResponse(Booking booking, String hospitalName, List<RideResponse> rides);
}
