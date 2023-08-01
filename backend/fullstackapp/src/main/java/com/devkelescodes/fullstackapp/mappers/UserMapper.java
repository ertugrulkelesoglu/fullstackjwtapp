package com.devkelescodes.fullstackapp.mappers;

import com.devkelescodes.fullstackapp.dto.SignUpDto;
import com.devkelescodes.fullstackapp.dto.UserDto;
import com.devkelescodes.fullstackapp.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto userToUserDto(User user);

    @Mapping(target = "password" , ignore = true)
    User signUpToUser(SignUpDto signUpDto);
}
