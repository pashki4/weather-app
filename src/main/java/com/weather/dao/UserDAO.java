package com.weather.dao;

import com.weather.model.User;

import java.util.Optional;

public interface UserDAO {
    Optional<User> get(Long id);

    void save(User user);
}
