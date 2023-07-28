package com.weather.dao;

import com.weather.model.Session;
import com.weather.model.User;

public interface SessionDAO extends DAO<Session> {
    public boolean isSessionExpired(User user);
}
