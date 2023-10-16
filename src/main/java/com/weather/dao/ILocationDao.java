package com.weather.dao;

import com.weather.model.Location;
import com.weather.model.User;

public interface ILocationDao {
    void addLocation(Location location);

    void remove(Location location);
}
