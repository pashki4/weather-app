package com.weather.dao;

import com.weather.model.User;

import java.util.Optional;

public interface UserDAO {
    Optional<User> getById(Long id);

    void save(User user);

    Optional<User> getByLogin(String login);
}
