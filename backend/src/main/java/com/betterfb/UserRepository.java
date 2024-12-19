package com.betterfb;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

/**
 * UserRepository is a stateless session bean that provides
 * database operations for the User entity.
 */
@Stateless
public class UserRepository {

    // Injects the EntityManager to interact with the persistence context
    @PersistenceContext
    private EntityManager em;

    /**
     * Saves a new User entity to the database.
     *
     * @param user the User entity to be persisted
     */
    public void save(User user) {
        em.persist(user);
    }

    /**
     * Finds a User entity by its username.
     *
     * @param username the username of the User to be retrieved
     * @return the User entity with the specified username
     */
    public User findByUsername(String username) {
        try {
            return em.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class).setParameter("username", username).getSingleResult();
        } catch (jakarta.persistence.NoResultException e) {
            return null;
        }
    }
}
