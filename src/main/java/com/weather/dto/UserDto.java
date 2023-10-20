package com.weather.dto;

import com.weather.model.Location;

import java.util.List;

public class UserDto {
    public Long id;
    public String login;
    public List<Location> locations;
}
