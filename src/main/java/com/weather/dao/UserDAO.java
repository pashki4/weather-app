package com.weather.dao;

import com.weather.exception.UserDaoException;
import com.weather.model.Location;
import com.weather.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.hibernate.Session;

import java.util.Optional;

public class UserDAO implements IUserDAO {

    private static final EntityManagerFactory emf
            = Persistence.createEntityManagerFactory("postgres");

    @Override
    public Optional<User> getByIdFetch(Long id) {
        EntityManager entityManager = emf.createEntityManager();
        entityManager.unwrap(Session.class).setDefaultReadOnly(true);
        entityManager.getTransaction().begin();
        try {
            User user = entityManager
                    .createQuery("SELECT u FROM User u LEFT JOIN FETCH u.locations WHERE u.id =: id", User.class)
                    .setParameter("id", id)
                    .getSingleResult();
            entityManager.getTransaction().commit();
            return Optional.ofNullable(user);
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw new UserDaoException(String.format("Error performing getByIdFetch( %d )", id), e);
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
            throw new UserDaoException(String.format("Error performing save( %s )", user), e);
        } finally {
            entityManager.close();
        }
    }

    @Override
    public Optional<User> getByLoginFetch(String login) {
        EntityManager entityManager = emf.createEntityManager();
        entityManager.unwrap(Session.class).setDefaultReadOnly(true);
        entityManager.getTransaction().begin();
        try {
            User user = entityManager
                    .createQuery("SELECT u FROM User u LEFT JOIN FETCH u.locations WHERE u.login =: login", User.class)
                    .setParameter("login", login)
                    .getSingleResult();
            entityManager.getTransaction().commit();
            return Optional.ofNullable(user);
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw new UserDaoException(String.format("Error performing getByLoginFetch( %s )", login), e);
        } finally {
            entityManager.close();
        }
    }

    @Override
    public void addLocation(Long id, Location location) {
        EntityManager entityManager = emf.createEntityManager();
        entityManager.getTransaction().begin();
        try {
            User referenceUser = entityManager.getReference(User.class, id);
            referenceUser.addLocation(location);
            entityManager.persist(location);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw new UserDaoException(String.format("Error performing addLocation( %d, %s ) ", id, location), e);
        } finally {
            entityManager.close();
        }
    }
}
