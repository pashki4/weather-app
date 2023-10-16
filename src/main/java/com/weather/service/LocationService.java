package com.weather.service;

import com.weather.dao.ILocationDao;
import com.weather.model.Location;

public class LocationService {
    private ILocationDao locationDao;

    public LocationService(ILocationDao locationDao) {
        this.locationDao = locationDao;
    }

    public void addLocation(Location location) {
        locationDao.addLocation(location);
    }

}
