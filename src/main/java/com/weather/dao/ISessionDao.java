package com.weather.dao;

import com.weather.model.Session;

import java.util.Optional;
import java.util.UUID;

public interface ISessionDao {

    Optional<Session> getSessionByUserId(Long userId);

    void saveByUserId(Long id);

    Optional<Session> getSessionById(UUID uuid);

    void removeByUserId(Long id);
}
