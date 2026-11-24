package com.careride.slot.mapper;

import com.careride.slot.dto.SlotResponse;
import com.careride.slot.entity.TimeSlot;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SlotMapper {

    @Mapping(target = "availableVehicles", expression = "java(slot.getCapacity() - slot.getBookedCount())")
    SlotResponse toResponse(TimeSlot slot);
}
