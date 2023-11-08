package com.weather.service;

import com.weather.dao.ISessionDao;
import com.weather.model.Session;
import com.weather.model.User;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public class SessionService {
    private final ISessionDao sessionDAO;

    public SessionService(ISessionDao sessionDAO) {
        this.sessionDAO = sessionDAO;
    }

    public Optional<Session> getSessionByUserId(Long userId) {
        return sessionDAO.getSessionByUserId(userId);
    }

    public Optional<Session> getSessionById(UUID uuid) {
        return sessionDAO.getSessionById(uuid);
    }

    public void saveSession(User user) {
        sessionDAO.saveForUser(user);
    }

    public boolean isSessionActive(Session session) {
        return LocalDateTime.now().isBefore(session.getExpiresAt());
    }

    public void remove(Long id) {
        sessionDAO.removeByUserId(id);
    }
}
