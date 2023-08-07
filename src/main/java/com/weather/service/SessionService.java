package com.weather.service;

import com.weather.dao.SessionDAO;
import com.weather.model.Session;

import java.util.Optional;

public class SessionService {
    private final SessionDAO SessionDAO;

    public SessionService(SessionDAO SessionDAO) {
        this.SessionDAO = SessionDAO;
    }

    public Optional<Session> getSessionByUserId(Long userId) {
        return SessionDAO.getSessionByUserId(userId);
    }

    public void save(Session session) {
        SessionDAO.save(session);
    }
}
