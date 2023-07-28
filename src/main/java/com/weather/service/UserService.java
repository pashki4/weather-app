package com.weather.service;

import com.weather.dao.UserDAO;
import com.weather.model.User;

import java.util.Optional;

public class UserService {
    private final UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public Optional<User> get(Long id) {
        return userDAO.get(id);
    }

    public void save(User user) {
        userDAO.save(user);
    }
}
