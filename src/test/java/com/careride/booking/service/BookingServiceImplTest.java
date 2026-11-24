package com.careride.booking.service;

import com.careride.booking.dto.BookingRequest;
import com.careride.booking.dto.BookingResponse;
import com.careride.booking.dto.RideRequest;
import com.careride.booking.dto.RideResponse;
import com.careride.booking.entity.Booking;
import com.careride.booking.entity.BookingStatus;
import com.careride.booking.entity.Direction;
import com.careride.booking.entity.MobilityNeed;
import com.careride.booking.entity.Ride;
import com.careride.booking.mapper.BookingMapper;
import com.careride.booking.mapper.RideMapper;
import com.careride.booking.repository.BookingRepository;
import com.careride.booking.repository.RideRepository;
import com.careride.common.exception.SlotFullException;
import com.careride.hospital.entity.Hospital;
import com.careride.hospital.repository.HospitalRepository;
import com.careride.slot.entity.TimeSlot;
import com.careride.slot.repository.TimeSlotRepository;
import com.careride.user.entity.Role;
import com.careride.user.entity.User;
import com.careride.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    private static final String EMAIL = "caretaker@example.com";

    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private RideRepository rideRepository;
    @Mock
    private TimeSlotRepository timeSlotRepository;
    @Mock
    private HospitalRepository hospitalRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private BookingMapper bookingMapper;
    @Mock
    private RideMapper rideMapper;

    @InjectMocks
    private BookingServiceImpl service;

    @Test
    void booksOneWayTrip() {
        TimeSlot slot = slot(10L, LocalDateTime.now().plusHours(1));
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(caretaker()));
        when(hospitalRepository.findById(2L)).thenReturn(Optional.of(hospital()));
        when(timeSlotRepository.findById(10L)).thenReturn(Optional.of(slot));
        when(rideRepository.existsBySlotIdAndBooking_Caretaker_Email(10L, EMAIL)).thenReturn(false);
        when(timeSlotRepository.incrementBookedCount(10L)).thenReturn(1);
        when(bookingRepository.save(any(Booking.class))).thenAnswer(invocation -> {
            Booking booking = invocation.getArgument(0);
            booking.setId(100L);
            return booking;
        });
        when(rideRepository.save(any(Ride.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(rideMapper.toResponse(any(Ride.class))).thenReturn(
                new RideResponse(Direction.HOME_TO_HOSPITAL, slot.getStartTime(), slot.getEndTime()));
        when(bookingMapper.toResponse(any(Booking.class), eq("City General Hospital"), anyList()))
                .thenReturn(new BookingResponse(100L, "City General Hospital", "Asha Verma", "9876543210",
                        "12 Main Street", MobilityNeed.WALKING, List.of(), BookingStatus.CONFIRMED));

        BookingResponse response = service.book(EMAIL, new BookingRequest(
                2L, "Asha Verma", "9876543210", "12 Main Street", MobilityNeed.WALKING, "None",
                List.of(new RideRequest(Direction.HOME_TO_HOSPITAL, 10L))));

        assertThat(response.id()).isEqualTo(100L);
        assertThat(response.patientName()).isEqualTo("Asha Verma");
        assertThat(response.status()).isEqualTo(BookingStatus.CONFIRMED);
        verify(timeSlotRepository).incrementBookedCount(10L);
        verify(bookingRepository).save(any(Booking.class));
    }

    @Test
    void throwsWhenSlotIsFull() {
        TimeSlot slot = slot(10L, LocalDateTime.now().plusHours(1));
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(caretaker()));
        when(hospitalRepository.findById(2L)).thenReturn(Optional.of(hospital()));
        when(timeSlotRepository.findById(10L)).thenReturn(Optional.of(slot));
        when(rideRepository.existsBySlotIdAndBooking_Caretaker_Email(10L, EMAIL)).thenReturn(false);
        when(timeSlotRepository.incrementBookedCount(10L)).thenReturn(0);

        assertThatThrownBy(() -> service.book(EMAIL, new BookingRequest(
                2L, "Asha Verma", "9876543210", "12 Main Street", MobilityNeed.WALKING, null,
                List.of(new RideRequest(Direction.HOME_TO_HOSPITAL, 10L)))))
                .isInstanceOf(SlotFullException.class)
                .hasMessage("Slot is full: 10");

        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    void rejectsRoundTripWithReturnBeforePickup() {
        TimeSlot outbound = slot(10L, LocalDateTime.now().plusHours(2));
        TimeSlot inbound = slot(11L, LocalDateTime.now().plusHours(1));
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(caretaker()));
        when(hospitalRepository.findById(2L)).thenReturn(Optional.of(hospital()));
        when(timeSlotRepository.findById(10L)).thenReturn(Optional.of(outbound));
        when(timeSlotRepository.findById(11L)).thenReturn(Optional.of(inbound));
        when(rideRepository.existsBySlotIdAndBooking_Caretaker_Email(10L, EMAIL)).thenReturn(false);
        when(rideRepository.existsBySlotIdAndBooking_Caretaker_Email(11L, EMAIL)).thenReturn(false);

        BookingRequest request = new BookingRequest(2L, "Asha Verma", "9876543210", "12 Main Street",
                MobilityNeed.WHEELCHAIR, "", List.of(
                new RideRequest(Direction.HOME_TO_HOSPITAL, 10L),
                new RideRequest(Direction.HOSPITAL_TO_HOME, 11L)));

        assertThatThrownBy(() -> service.book(EMAIL, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Return slot must start after the pickup slot ends");

        verify(timeSlotRepository, never()).incrementBookedCount(anyLong());
        verify(bookingRepository, never()).save(any(Booking.class));
    }

    private User caretaker() {
        return User.builder()
                .id(1L)
                .name("Rahul Verma")
                .email(EMAIL)
                .phone("9876543210")
                .password("hash")
                .role(Role.CARETAKER)
                .build();
    }

    private Hospital hospital() {
        return Hospital.builder()
                .id(2L)
                .name("City General Hospital")
                .address("100 Health Avenue")
                .city("Bengaluru")
                .build();
    }

    private TimeSlot slot(Long id, LocalDateTime startTime) {
        return TimeSlot.builder()
                .id(id)
                .startTime(startTime)
                .endTime(startTime.plusMinutes(30))
                .capacity(3)
                .bookedCount(0)
                .build();
    }
}
