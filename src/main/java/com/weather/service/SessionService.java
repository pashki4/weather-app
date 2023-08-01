package com.weather.service;

import com.weather.dao.SessionDAO;
import com.weather.model.Session;
import com.weather.model.User;

import java.util.Optional;

public class SessionService {
    private final SessionDAO sessionDAO;

    public SessionService(SessionDAO sessionDAO) {
        this.sessionDAO = sessionDAO;
    }

    public Optional<Session> getSession(User user) {
        return sessionDAO.getSession(user);
    }

    public void save(Session session) {
        sessionDAO.save(session);
    }
}
