package com.weather.service;

import com.weather.dao.ISessionDAO;
import com.weather.model.Session;
import com.weather.model.User;

import java.util.Optional;

public class SessionService {
    private final ISessionDAO ISessionDAO;

    public SessionService(ISessionDAO ISessionDAO) {
        this.ISessionDAO = ISessionDAO;
    }

    public Optional<Session> getSessionByUserId(Long userId) {
        return ISessionDAO.getSessionByUserId(userId);
    }

    public void saveSession(User user) {
        ISessionDAO.saveForUser(user);
    }
}
