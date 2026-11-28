package com.careride.slot.controller;

import com.careride.common.exception.dto.ErrorResponse;
import com.careride.slot.dto.SlotResponse;
import com.careride.slot.service.SlotService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/slots")
@RequiredArgsConstructor
@Tag(name = "Slots", description = "Check available pickup slots")
public class SlotController {

    private final SlotService slotService;

    @GetMapping
    @Operation(summary = "Get available slots", description = "Returns future pickup slots for the given date with remaining vehicle capacity. Requires JWT Bearer token.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Slots returned successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid or missing date parameter",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Authentication required",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public List<SlotResponse> getAvailableSlots(
            @Parameter(description = "Date to search slots for (yyyy-MM-dd)", required = true, example = "2026-11-28")
            @RequestParam @NotNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return slotService.getAvailableSlots(date);
    }
}
