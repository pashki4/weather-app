package com.weather.service;

import com.weather.dao.ILocationDao;
import com.weather.model.Location;

public class LocationService {
    private final ILocationDao locationDao;

    public LocationService(ILocationDao locationDao) {
        this.locationDao = locationDao;
    }

    public void add(Location location) {
        locationDao.addLocation(location);
    }

    public void remove(Location location) {
        locationDao.remove(location);
    }

}
