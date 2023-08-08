package com.weather.dao;

import com.weather.exception.SessionDaoException;
import com.weather.model.Session;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.Optional;

public class SessionDAO implements ISessionDAO {
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("postgres");

    @Override
    public Optional<Session> getSessionByUserId(Long userId) {
        EntityManager entityManager = emf.createEntityManager();
        entityManager.getTransaction().begin();
        try {
            Optional<Session> session = entityManager
                    .createQuery("SELECT s FROM Session s WHERE s.user.id =: id " +
                            "ORDER BY s.expiresAt DESC LIMIT 1", Session.class)
                    .setParameter("id", userId)
                    .getResultStream()
                    .findAny();
            entityManager.getTransaction().commit();
            return session;
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw new SessionDaoException(String.format("Cannot perform getSessionByUserId( %d )", userId), e);
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
