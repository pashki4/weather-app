package com.weather.service;

import com.weather.dao.DAO;
import com.weather.exception.SessionDaoException;
import com.weather.model.Session;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

public class SessionService {
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("postgres");
    private final DAO<Session> sessionDAO;

    public SessionService(DAO<Session> sessionDAO) {
        this.sessionDAO = sessionDAO;
    }

    public Optional<Session> findSessionById(UUID uuid) {
        return sessionDAO.get(uuid);
    }

    public boolean isSessionExpired(UUID uuid) {
        EntityManager entityManager = emf.createEntityManager();
        entityManager.unwrap(org.hibernate.Session.class).setDefaultReadOnly(true);
        entityManager.getTransaction().begin();
        try {
            LocalDateTime expiresAt = entityManager
                    .createQuery("SELECT s FROM Session s WHERE s.id =: id", Session.class)
                    .setParameter("id", uuid)
                    .getSingleResult()
                    .getExpiresAt();

            LocalDateTime currentTime = entityManager
                    .createQuery("SELECT CURRENT_TIMESTAMP", LocalDateTime.class)
                    .getSingleResult();
            entityManager.getTransaction().commit();

            return expiresAt.until(currentTime, ChronoUnit.NANOS) >= 0;
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw new SessionDaoException("Cannot perform dao operation", e);
        } finally {
            entityManager.close();
        }
    }
}
