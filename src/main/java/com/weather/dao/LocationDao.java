package com.weather.dao;

import com.weather.exception.LocationDaoException;
import com.weather.model.Location;
import com.weather.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class LocationDao implements ILocationDao {
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("postgres");

    @Override
    public void addLocationToUser(Location location, User user) {
        EntityManager entityManager = emf.createEntityManager();
        entityManager.getTransaction().begin();
        try {
            entityManager
                    .createNativeQuery("INSERT INTO locations(name, user_id, latitude, longitude) VALUES (?, ?, ?, ?)")
                    .setParameter(1, location.getName())
                    .setParameter(2, user.getId())
                    .setParameter(3, location.getLongitude())
                    .setParameter(4, location.getLatitude());
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw new LocationDaoException("Error performing addLocationToUser() for user: " + user + " and location: " + location, e);
        } finally {
            entityManager.close();
        }
    }
}
