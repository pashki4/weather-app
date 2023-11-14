package com.weather.dao;

import com.weather.exception.SessionDaoException;
import com.weather.model.Session;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.Optional;
import java.util.UUID;

public class SessionDao implements ISessionDao {

    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("postgres");

    @Override
    public Optional<Session> getSessionByUserId(Long userId) {
        EntityManager entityManager = emf.createEntityManager();
        entityManager.unwrap(org.hibernate.Session.class).setDefaultReadOnly(true);
        entityManager.getTransaction().begin();
        try {
            Optional<Session> session = entityManager
                    .createQuery("SELECT s FROM Session s WHERE s.user.id =: id "
                                 + "ORDER BY s.expiresAt DESC LIMIT 1", Session.class)
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
    public void saveByUserId(Long id) {
        EntityManager entityManager = emf.createEntityManager();
        entityManager.getTransaction().begin();
        try {
            entityManager.createNativeQuery("INSERT INTO sessions(user_id) VALUES (?)")
                    .setParameter(1, id)
                    .executeUpdate();
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw new SessionDaoException(String.format("Cannot perform saveByUserId(%d)", id), e);
        } finally {
            entityManager.close();
        }
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

    @Override
    public void removeByUserId(Long userId) {
        EntityManager entityManager = emf.createEntityManager();
        entityManager.getTransaction().begin();
        try {
            entityManager.createQuery("DELETE FROM Session s WHERE s.user.id =: id")
                    .setParameter("id", userId)
                    .executeUpdate();
            entityManager.getTransaction().commit();
        } catch (RuntimeException e) {
            entityManager.getTransaction().rollback();
            throw new SessionDaoException(String.format("Error performing dao operation removeByUserId( %d )", userId), e);
        } finally {
            entityManager.close();
        }
    }
}
