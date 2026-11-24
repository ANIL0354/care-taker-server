package com.careride.booking.repository;

import com.careride.booking.entity.Ride;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RideRepository extends JpaRepository<Ride, Long> {

    boolean existsBySlotIdAndBooking_Caretaker_Email(Long slotId, String email);
}
