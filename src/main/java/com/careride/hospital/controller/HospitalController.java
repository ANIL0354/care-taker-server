package com.careride.hospital.controller;

import com.careride.common.exception.dto.ErrorResponse;
import com.careride.hospital.dto.HospitalResponse;
import com.careride.hospital.service.HospitalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/hospitals")
@RequiredArgsConstructor
@Tag(name = "Hospitals", description = "List available hospitals")
public class HospitalController {

    private final HospitalService hospitalService;

    @GetMapping
    @Operation(summary = "List hospitals", description = "Returns all hospitals available for transport bookings. Requires JWT Bearer token.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Hospitals returned successfully"),
            @ApiResponse(responseCode = "401", description = "Authentication required",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public List<HospitalResponse> listHospitals() {
        return hospitalService.listHospitals();
    }
}
