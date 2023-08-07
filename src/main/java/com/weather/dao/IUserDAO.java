package com.weather.dao;

import com.weather.model.User;

import java.util.Optional;

public interface IUserDAO {
    Optional<User> getById(Long id);

    User save(User user);

    Optional<User> getByLogin(String login);

    Optional<User> create(User user);

}
