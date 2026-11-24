package com.careride.booking.service;

import com.careride.booking.dto.BookingRequest;
import com.careride.booking.dto.BookingResponse;
import com.careride.booking.dto.RideRequest;
import com.careride.booking.dto.RideResponse;
import com.careride.booking.entity.Booking;
import com.careride.booking.entity.BookingStatus;
import com.careride.booking.entity.Direction;
import com.careride.booking.entity.Ride;
import com.careride.booking.entity.RideStatus;
import com.careride.booking.mapper.BookingMapper;
import com.careride.booking.mapper.RideMapper;
import com.careride.booking.repository.BookingRepository;
import com.careride.booking.repository.RideRepository;
import com.careride.common.exception.ResourceNotFoundException;
import com.careride.common.exception.SlotFullException;
import com.careride.hospital.entity.Hospital;
import com.careride.hospital.repository.HospitalRepository;
import com.careride.slot.entity.TimeSlot;
import com.careride.slot.repository.TimeSlotRepository;
import com.careride.user.entity.User;
import com.careride.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final RideRepository rideRepository;
    private final TimeSlotRepository timeSlotRepository;
    private final HospitalRepository hospitalRepository;
    private final UserRepository userRepository;
    private final BookingMapper bookingMapper;
    private final RideMapper rideMapper;

    @Override
    @Transactional
    public BookingResponse book(String caretakerEmail, BookingRequest request) {
        List<RideRequest> requestedRides = request.rides();
        if (requestedRides == null || requestedRides.isEmpty() || requestedRides.size() > 2) {
            throw new IllegalArgumentException("A booking must have one or two rides");
        }

        Set<Direction> directions = requestedRides.stream()
                .map(RideRequest::direction)
                .collect(Collectors.toSet());
        if (directions.size() != requestedRides.size()) {
            throw new IllegalArgumentException("The two rides must have different directions");
        }

        Set<Long> slotIds = new LinkedHashSet<>();
        for (RideRequest rideRequest : requestedRides) {
            if (!slotIds.add(rideRequest.slotId())) {
                throw new IllegalArgumentException("A caretaker cannot book two rides in the same slot");
            }
        }

        User caretaker = userRepository.findByEmail(caretakerEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Caretaker not found"));
        Hospital hospital = hospitalRepository.findById(request.hospitalId())
                .orElseThrow(() -> new ResourceNotFoundException("Hospital not found"));

        LocalDateTime now = LocalDateTime.now();
        Map<Long, TimeSlot> slots = new LinkedHashMap<>();
        for (Long slotId : slotIds) {
            TimeSlot slot = timeSlotRepository.findById(slotId)
                    .orElseThrow(() -> new ResourceNotFoundException("Slot not found: " + slotId));
            if (!slot.getStartTime().isAfter(now)) {
                throw new IllegalArgumentException("Slot has already started");
            }
            if (rideRepository.existsBySlotIdAndBooking_Caretaker_Email(slotId, caretakerEmail)) {
                throw new IllegalArgumentException("You already have a ride in this slot");
            }
            slots.put(slotId, slot);
        }

        if (directions.contains(Direction.HOME_TO_HOSPITAL) && directions.contains(Direction.HOSPITAL_TO_HOME)) {
            RideRequest outbound = requestedRides.stream()
                    .filter(rideRequest -> rideRequest.direction() == Direction.HOME_TO_HOSPITAL)
                    .findFirst()
                    .orElseThrow();
            RideRequest inbound = requestedRides.stream()
                    .filter(rideRequest -> rideRequest.direction() == Direction.HOSPITAL_TO_HOME)
                    .findFirst()
                    .orElseThrow();
            TimeSlot outboundSlot = slots.get(outbound.slotId());
            TimeSlot inboundSlot = slots.get(inbound.slotId());
            if (!inboundSlot.getStartTime().isAfter(outboundSlot.getEndTime())) {
                throw new IllegalArgumentException("Return slot must start after the pickup slot ends");
            }
        }

        for (Long slotId : slotIds) {
            int updated = timeSlotRepository.incrementBookedCount(slotId);
            if (updated == 0) {
                throw new SlotFullException("Slot is full: " + slotId);
            }
        }

        Booking booking = bookingRepository.save(Booking.builder()
                .caretaker(caretaker)
                .hospital(hospital)
                .patientName(request.patientName())
                .patientPhone(request.patientPhone())
                .homeAddress(request.homeAddress())
                .mobilityNeed(request.mobilityNeed())
                .notes(request.notes())
                .status(BookingStatus.CONFIRMED)
                .createdAt(LocalDateTime.now())
                .build());

        List<RideResponse> rideResponses = new ArrayList<>();
        for (RideRequest rideRequest : requestedRides) {
            Ride ride = rideRepository.save(Ride.builder()
                    .booking(booking)
                    .direction(rideRequest.direction())
                    .slot(slots.get(rideRequest.slotId()))
                    .status(RideStatus.CONFIRMED)
                    .build());
            rideResponses.add(rideMapper.toResponse(ride));
        }

        return bookingMapper.toResponse(booking, hospital.getName(), rideResponses);
    }
}
