package com.careride.hospital.service;

import com.careride.hospital.dto.HospitalResponse;

import java.util.List;

public interface HospitalService {

    List<HospitalResponse> listHospitals();
}
