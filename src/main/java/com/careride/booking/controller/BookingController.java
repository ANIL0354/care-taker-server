package com.careride.booking.controller;

import com.careride.booking.dto.BookingRequest;
import com.careride.booking.dto.BookingResponse;
import com.careride.booking.service.BookingService;
import com.careride.common.exception.dto.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Bookings", description = "Create patient transport bookings")
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    @PreAuthorize("hasRole('CARETAKER')")
    @Operation(summary = "Book a ride", description = "Creates a one-way or round-trip booking for a patient. Requires CARETAKER role and JWT Bearer token. The authenticated user is derived from the token.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Booking created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request, slot mismatch, or unavailable slot",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Authentication required",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Authenticated user is not a CARETAKER",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Hospital or slot not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Slot is full",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public BookingResponse book(@io.swagger.v3.oas.annotations.Parameter(hidden = true) Authentication authentication,
                                @Valid @RequestBody BookingRequest request) {
        return bookingService.book(authentication.getName(), request);
    }
}
