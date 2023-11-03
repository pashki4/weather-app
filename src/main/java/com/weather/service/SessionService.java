package com.weather.service;

import com.weather.dao.ISessionDAO;
import com.weather.model.Session;
import com.weather.model.User;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

public class SessionService {
    private final ISessionDAO sessionDAO;

    public SessionService(ISessionDAO sessionDAO) {
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

    public boolean isSessionExpired(Session session) {
        return !session.getExpiresAt().isBefore(LocalDateTime.now(ZoneId.of("UTC")));
    }

    public void remove(Long id) {
        sessionDAO.removeByUserId(id);
    }
}
