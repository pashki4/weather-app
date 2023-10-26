package com.weather.dto;

import com.weather.model.Location;

import java.util.Set;

public class UserDto {
    public Long id;
    public String login;
    public Set<Location> locations;
}
