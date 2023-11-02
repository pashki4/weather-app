package com.weather.mapper;

import com.weather.dto.UserDto;
import com.weather.model.User;

public class UserMapper implements Mapper<User, UserDto> {
    @Override
    public UserDto map(User object) {
        return UserDto.builder()
                .id(object.getId())
                .login(object.getLogin())
                .locations(object.getLocations())
                .build();
    }
}
