package com.weather.service;

import com.weather.dao.ISessionDAO;
import com.weather.model.Session;

import java.util.Optional;

public class SessionService {
    private final ISessionDAO ISessionDAO;

    public SessionService(ISessionDAO ISessionDAO) {
        this.ISessionDAO = ISessionDAO;
    }

    public Optional<Session> getSessionByUserId(Long userId) {
        return ISessionDAO.getSessionByUserId(userId);
    }

    public void save(Session session) {
        ISessionDAO.save(session);
    }
}
