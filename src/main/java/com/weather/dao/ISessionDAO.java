package com.weather.dao;

import com.weather.model.Session;
import com.weather.model.User;

import java.util.Optional;

public interface ISessionDAO {

    Optional<Session> getSessionByUserId(Long userId);

    void saveForUser(User user);

    void invalidate(User user);

}
