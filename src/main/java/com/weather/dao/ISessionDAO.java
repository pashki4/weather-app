package com.weather.dao;

import com.weather.exception.SessionDaoException;
import com.weather.model.Session;
import com.weather.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ISessionDAO implements SessionDAO {
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("postgres");

    @Override
    public boolean isSessionExpired(User user) {
        EntityManager entityManager = emf.createEntityManager();
        entityManager.getTransaction().begin();
        try {
            Optional<Boolean> id = entityManager
                    .createQuery("SELECT CASE WHEN s.expiresAt > NOW() THEN false ELSE true END AS is_expired " +
                            "FROM Session s WHERE s.user.id =: id ORDER BY s.expiresAt DESC LIMIT 1", Boolean.class)
                    .setParameter("id", user.getId())
                    .getResultStream()
                    .findAny();
            entityManager.getTransaction().commit();
            return id.orElse(true);
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw new SessionDaoException(String.format("Cannot perform isSessionExpiredForUser( %s )", user), e);
        } finally {
            entityManager.close();
        }
    }

    @Override
    public void save(Session session) {
        EntityManager entityManager = emf.createEntityManager();
        entityManager.getTransaction().begin();
        try {
            entityManager.persist(session);
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw new SessionDaoException(String.format("Cannot perform save( %s )", session), e);
        } finally {
            entityManager.close();
        }
    }
}
