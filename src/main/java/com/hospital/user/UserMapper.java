package com.hospital.user;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "failedLoginAttempts", ignore = true)
    @Mapping(target = "lockedUntil", ignore = true)
    @Mapping(target = "accountNonLocked", ignore = true)
    @Mapping(target = "enabled", ignore = true)
    User toEntity(UserRequest request);

    UserResponse toResponse(User user);

    @Mapping(target = "failedLoginAttempts", ignore = true)
    @Mapping(target = "lockedUntil", ignore = true)
    void updateEntity(UserRequest request, @MappingTarget User user);
}
