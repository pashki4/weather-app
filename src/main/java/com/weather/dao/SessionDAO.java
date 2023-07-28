package com.weather.dao;

import com.weather.model.Session;
import com.weather.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SessionDAO {

    public boolean isSessionExpired(User user);
    void save(Session session);

}
