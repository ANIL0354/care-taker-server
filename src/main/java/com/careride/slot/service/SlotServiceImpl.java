package com.careride.slot.service;

import com.careride.slot.dto.SlotResponse;
import com.careride.slot.mapper.SlotMapper;
import com.careride.slot.repository.TimeSlotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SlotServiceImpl implements SlotService {

    private final TimeSlotRepository timeSlotRepository;
    private final SlotMapper slotMapper;

    @Override
    @Transactional(readOnly = true)
    public List<SlotResponse> getAvailableSlots(LocalDate date) {
        LocalDateTime dayStart = date.atStartOfDay();
        LocalDateTime dayEnd = date.plusDays(1).atStartOfDay();
        return timeSlotRepository.findAvailableSlots(dayStart, dayEnd, LocalDateTime.now()).stream()
                .map(slotMapper::toResponse)
                .toList();
    }
}
