package com.careride.slot.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "time_slots")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TimeSlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "slot_start", nullable = false, unique = true)
    private LocalDateTime startTime;

    @Column(name = "slot_end", nullable = false)
    private LocalDateTime endTime;

    @Column(name = "capacity", nullable = false)
    private int capacity;

    @Builder.Default
    @Column(name = "booked_count", nullable = false)
    private int bookedCount = 0;

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof TimeSlot timeSlot)) {
            return false;
        }
        return id != null && id.equals(timeSlot.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
