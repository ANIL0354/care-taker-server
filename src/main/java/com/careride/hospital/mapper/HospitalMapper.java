package com.careride.hospital.mapper;

import com.careride.hospital.dto.HospitalResponse;
import com.careride.hospital.entity.Hospital;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface HospitalMapper {

    HospitalResponse toResponse(Hospital hospital);
}
