package com.weather.dto;

import com.weather.model.Location;
import lombok.Builder;
import lombok.Value;

import java.util.Set;

@Value
@Builder
public class UserDto {
    public Long id;
    public String login;
    public Set<Location> locations;
}
