package com.weather.service;

import com.weather.dao.UserDAO;
import com.weather.model.User;

import java.util.Optional;

public class UserService {
    private final UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public Optional<User> getById(Long id) {
        return userDAO.getById(id);
    }

    public Optional<User> getByLogin(String login) {
        return userDAO.getByLogin(login);
    }

    public void save(User user) {
        userDAO.save(user);
    }
}
