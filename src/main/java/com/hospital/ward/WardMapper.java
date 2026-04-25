package com.hospital.ward;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WardMapper { default WardResponse toResponse(Ward ward) { return WardResponse.from(ward); } }
