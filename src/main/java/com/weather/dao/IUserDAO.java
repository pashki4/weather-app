package com.weather.dao;

import com.weather.exception.SessionDaoException;
import com.weather.exception.UserDaoException;
import com.weather.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.hibernate.Session;

import java.util.Optional;

public class IUserDAO implements UserDAO {

    private static final EntityManagerFactory emf
            = Persistence.createEntityManagerFactory("postgres");

    @Override
    public Optional<User> get(Long id) {
        EntityManager entityManager = emf.createEntityManager();
        entityManager.unwrap(Session.class).setDefaultReadOnly(true);
        entityManager.getTransaction().begin();
        try {
            User user = entityManager.find(User.class, id);
            entityManager.getTransaction().commit();
            return Optional.ofNullable(user);
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw new UserDaoException(String.format("Cannot perform get( %d )", id), e);
        } finally {
            entityManager.close();
        }
    }

    @Override
    public void save(User user) {
        EntityManager entityManager = emf.createEntityManager();
        entityManager.getTransaction().begin();
        try {
            entityManager.persist(user);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw new UserDaoException(String.format("Cannot perform save( %s )", user), e);
        } finally {
            entityManager.close();
        }
    }
}
