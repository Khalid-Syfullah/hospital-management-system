package com.hospital.ward;

import com.hospital.ward.WardDtos.BedResponse;
import com.hospital.ward.WardDtos.WardResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface WardMapper {
    WardResponse toResponse(Ward ward);

    @Mapping(target = "wardId", source = "ward.id")
    @Mapping(target = "patientId", source = "patient.id")
    BedResponse toResponse(Bed bed);
}
