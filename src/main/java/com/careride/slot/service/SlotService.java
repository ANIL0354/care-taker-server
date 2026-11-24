package com.careride.slot.service;

import com.careride.slot.dto.SlotResponse;

import java.time.LocalDate;
import java.util.List;

public interface SlotService {

    List<SlotResponse> getAvailableSlots(LocalDate date);
}
