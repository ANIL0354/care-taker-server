package com.careride.slot.repository;

import com.careride.slot.entity.TimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface TimeSlotRepository extends JpaRepository<TimeSlot, Long> {

    @Query("""
            select s from TimeSlot s
            where s.startTime > :now
              and s.startTime >= :dayStart
              and s.startTime < :dayEnd
              and s.bookedCount < s.capacity
            order by s.startTime
            """)
    List<TimeSlot> findAvailableSlots(@Param("dayStart") LocalDateTime dayStart,
                                      @Param("dayEnd") LocalDateTime dayEnd,
                                      @Param("now") LocalDateTime now);

    @Modifying(flushAutomatically = true)
    @Query("update TimeSlot s set s.bookedCount = s.bookedCount + 1 " +
            "where s.id = :id and s.bookedCount < s.capacity")
    int incrementBookedCount(@Param("id") Long id);
}
