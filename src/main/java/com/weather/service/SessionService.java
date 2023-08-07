package com.weather.service;

import com.weather.dao.SessionDAO;
import com.weather.model.Session;

import java.util.Optional;

public class SessionService {
    private final SessionDAO sessionDAO;

    public SessionService(SessionDAO sessionDAO) {
        this.sessionDAO = sessionDAO;
    }

    public Optional<Session> getSessionByUserId(Long userId) {
        return sessionDAO.getSessionByUserId(userId);
    }

    public void save(Session session) {
        sessionDAO.save(session);
    }
}
