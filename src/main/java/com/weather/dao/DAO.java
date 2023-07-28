package com.weather.dao;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DAO<T> {

    Optional<T> get(UUID id);

    List<T> getAll();

    void save(T t);

    void delete(T t);
}
