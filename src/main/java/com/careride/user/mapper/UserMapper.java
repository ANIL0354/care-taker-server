package com.careride.user.mapper;

import com.careride.user.dto.UserResponse;
import com.careride.user.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponse toResponse(User user);
}
