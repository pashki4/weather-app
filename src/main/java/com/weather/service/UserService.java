package com.weather.service;

import com.weather.dao.IUserDAO;
import com.weather.model.User;

import java.util.Optional;

public class UserService {
    private final IUserDAO IUserDAO;

    public UserService(IUserDAO IUserDAO) {
        this.IUserDAO = IUserDAO;
    }

    public Optional<User> getById(Long id) {
        return IUserDAO.getByIdFetch(id);
    }

    public Optional<User> getByLogin(String login) {
        return IUserDAO.getByLoginFetch(login);
    }

    public void save(User user) {
        IUserDAO.save(user);
    }
}
