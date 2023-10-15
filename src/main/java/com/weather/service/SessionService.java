package com.weather.service;

import com.weather.dao.ISessionDAO;
import com.weather.model.Session;
import com.weather.model.User;

import java.util.Optional;
import java.util.UUID;

public class SessionService {
    private final ISessionDAO ISessionDAO;

    public SessionService(ISessionDAO ISessionDAO) {
        this.ISessionDAO = ISessionDAO;
    }

    public Optional<Session> getSessionByUserId(Long userId) {
        return ISessionDAO.getSessionByUserId(userId);
    }

    public Optional<Session> getSessionById(UUID uuid) {
        return ISessionDAO.getSessionById(uuid);
    }

    public void saveSession(User user) {
        ISessionDAO.saveForUser(user);
    }
}
