package com.weather.dao;

import com.weather.model.Session;
import com.weather.model.User;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.Optional;
import java.util.UUID;

public interface ISessionDao {

    Optional<Session> getSessionByUserId(Long userId);

    void saveForUser(User user);

    Optional<Session> getSessionById(UUID uuid);

    void removeByUserId(Long id);
}
