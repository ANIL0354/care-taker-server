package com.careride.booking.controller;

import com.careride.booking.dto.BookingRequest;
import com.careride.booking.dto.BookingResponse;
import com.careride.booking.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    @PreAuthorize("hasRole('CARETAKER')")
    public BookingResponse book(Authentication authentication, @Valid @RequestBody BookingRequest request) {
        return bookingService.book(authentication.getName(), request);
    }
}
