package com.weather.service;

import com.weather.dao.ISessionDao;
import com.weather.model.Session;

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

    public void saveSessionByUserId(Long id) {
        sessionDAO.saveByUserId(id);
    }

    public boolean isSessionActive(Session session) {
        return LocalDateTime.now().isBefore(session.getExpiresAt());
    }

    public void updateSessionByUserId(Long id) {
        sessionDAO.removeByUserId(id);
        sessionDAO.saveByUserId(id);
    }
}
