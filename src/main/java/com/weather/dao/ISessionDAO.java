package com.weather.dao;

import com.weather.model.Session;

import java.util.Optional;

public interface ISessionDAO {

    Optional<Session> getSessionByUserId(Long userId);

    void save(Session session);

}
