package com.weather.dao;

import com.weather.model.User;

import java.util.Optional;

public interface IUserDAO {
    Optional<User> getByIdFetch(Long id);

    void save(User user);

    Optional<User> getByLoginFetch(String login);

    Optional<User> create(User user);

}
