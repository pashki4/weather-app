package com.weather.dao;

import com.weather.model.Location;
import com.weather.model.User;

import java.util.Optional;

public interface IUserDAO {
    Optional<User> getByIdFetch(Long id);

    void save(User user);

    Optional<User> getByLoginFetch(String login);

    void addLocation(Long id, Location location);

    void removeLocation(Long userId, Long locationId);
}
