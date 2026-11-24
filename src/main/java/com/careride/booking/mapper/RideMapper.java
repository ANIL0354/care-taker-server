package com.careride.booking.mapper;

import com.careride.booking.dto.RideResponse;
import com.careride.booking.entity.Ride;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RideMapper {

    @Mapping(target = "startTime", source = "slot.startTime")
    @Mapping(target = "endTime", source = "slot.endTime")
    RideResponse toResponse(Ride ride);
}
