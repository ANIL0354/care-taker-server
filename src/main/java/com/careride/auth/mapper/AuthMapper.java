package com.careride.auth.mapper;

import com.careride.auth.dto.AuthResponse;
import com.careride.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AuthMapper {

    @Mapping(target = "token", ignore = true)
    AuthResponse toResponse(User user);
}
