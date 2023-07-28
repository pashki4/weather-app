package com.weather.service;

import com.weather.dao.SessionDAO;
import com.weather.model.Session;
import com.weather.model.User;

public class SessionService {
    private final SessionDAO sessionDAO;

    public SessionService(SessionDAO sessionDAO) {
        this.sessionDAO = sessionDAO;
    }

    public boolean isSessionExpired(User user) {
        return sessionDAO.isSessionExpired(user);
    }

    public void save(Session session) {
        sessionDAO.save(session);
    }
}
