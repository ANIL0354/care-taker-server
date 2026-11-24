package com.careride.hospital.service;

import com.careride.hospital.dto.HospitalResponse;
import com.careride.hospital.mapper.HospitalMapper;
import com.careride.hospital.repository.HospitalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HospitalServiceImpl implements HospitalService {

    private final HospitalRepository hospitalRepository;
    private final HospitalMapper hospitalMapper;

    @Override
    @Transactional(readOnly = true)
    public List<HospitalResponse> listHospitals() {
        return hospitalRepository.findAllByOrderByNameAsc().stream()
                .map(hospitalMapper::toResponse)
                .toList();
    }
}
