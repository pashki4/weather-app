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

public class ISessionDAO implements DAO<Session> {
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("postgres");

    @Override
    public Optional<Session> get(UUID id) {
        EntityManager entityManager = emf.createEntityManager();
        entityManager.unwrap(org.hibernate.Session.class).setDefaultReadOnly(true);
        entityManager.getTransaction().begin();
        try {
            Optional<Session> result = Optional.ofNullable(entityManager.find(Session.class, id));
            entityManager.getTransaction().commit();
            return result;
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw new SessionDaoException(String.format("Cannot perform dao operation: get( %d )", id), e);
        } finally {
            entityManager.close();
        }
    }

    @Override
    public List<Session> getAll() {
        EntityManager entityManager = emf.createEntityManager();
        entityManager.unwrap(org.hibernate.Session.class).setDefaultReadOnly(true);
        entityManager.getTransaction().begin();
        try {
            var result = entityManager.createQuery("SELECT s FROM Session s", Session.class)
                    .getResultList();
            entityManager.getTransaction().commit();
            return result;
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw new SessionDaoException("Cannot perform dao operation: getAll()", e);
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
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw new SessionDaoException(String.format("Cannot perform dao operation: save( %s )", session), e);
        } finally {
            entityManager.close();
        }
    }

    @Override
    public void delete(Session session) {
        EntityManager entityManager = emf.createEntityManager();
        entityManager.getTransaction().begin();
        try {
            entityManager.remove(session);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw new SessionDaoException(String.format("Cannot perform dao operation: delete( %s )", session), e);
        } finally {
            entityManager.close();
        }
    }

    public boolean isSessionExpiredForUser(User user) {
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
}
