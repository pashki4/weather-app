package com.weather.dao;

import com.weather.exception.SessionDaoException;
import com.weather.model.Session;
import com.weather.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.Optional;
import java.util.UUID;

public class SessionDAO implements ISessionDAO {
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("postgres");

    @Override
    public Optional<Session> getSessionByUserId(Long userId) {
        EntityManager entityManager = emf.createEntityManager();
        entityManager.unwrap(org.hibernate.Session.class).setDefaultReadOnly(true);
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
    public void saveForUser(User user) {
        EntityManager entityManager = emf.createEntityManager();
        entityManager.getTransaction().begin();
        try {
            entityManager.createNativeQuery("INSERT INTO sessions(user_id) VALUES (?)")
                    .setParameter(1, user.getId())
                    .executeUpdate();
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw new SessionDaoException(String.format("Cannot perform saveForUser( %s )", user), e);
        } finally {
            entityManager.close();
        }
    }

    @Override
    public void invalidate(User user) {

    }

    @Override
    public Optional<Session> getSessionById(UUID uuid) {
        EntityManager entityManager = emf.createEntityManager();
        entityManager.unwrap(org.hibernate.Session.class).setDefaultReadOnly(true);
        entityManager.getTransaction().begin();
        try {
            Session session = entityManager.find(Session.class, uuid);
            entityManager.getTransaction().commit();
            return Optional.ofNullable(session);
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw new SessionDaoException(String.format("Cannot perform getSessionById( %s )", uuid), e);
        } finally {
            entityManager.close();
        }
    }
}
