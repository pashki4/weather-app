package com.weather.dao;

import com.weather.model.Session;
import com.weather.model.User;

import java.util.Optional;

public interface SessionDAO {

    Optional<Session> getSession(User user);

    void save(Session session);

}
