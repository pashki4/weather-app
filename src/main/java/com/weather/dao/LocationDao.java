package com.weather.dao;

import com.weather.exception.LocationDaoException;
import com.weather.model.Location;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class LocationDao implements ILocationDao {
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("postgres");

    @Override
    public void addLocation(Location location) {
        EntityManager entityManager = emf.createEntityManager();
        entityManager.getTransaction().begin();
        try {
            entityManager.persist(location);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw new LocationDaoException(String.format("Error performing add( %s )", location), e);
        } finally {
            entityManager.close();
        }
    }

    @Override
    public void remove(Location location) {
        EntityManager entityManager = emf.createEntityManager();
        entityManager.getTransaction().begin();
        try {
            Location merged = entityManager.merge(location);
            entityManager.remove(merged);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw new LocationDaoException(String.format("Error performing remove( %s )", location), e);
        } finally {
            entityManager.close();
        }
    }
}
