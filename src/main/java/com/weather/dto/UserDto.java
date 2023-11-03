package com.weather.dto;

import com.weather.model.Location;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class UserDto {
    public Long id;
    public String login;
    public List<Location> locations;
}
