package com.betterfb;

import java.time.LocalDateTime;
import java.util.Optional;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

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

    /**
     * Finds a User entity by its email.
     *
     * @param email the email of the User to be retrieved
     * @return the User entity with the specified email
     */
    public User findByEmail(String email) {
        try {
            System.out.println("Searching for user with email: " + email);
            User user = em.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class)
                          .setParameter("email", email)
                          .getSingleResult();
            System.out.println("User found: " + user.getUsername());
            return user;
        } catch (jakarta.persistence.NoResultException e) {
            System.out.println("No user found with email: " + email);
            return null;
        } catch (Exception e) {
            System.err.println("Error querying user by email: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    

    /**
     * Finds a User entity by its reset token.
     *
     * @param token the reset token of the User to be retrieved
     * @return the User entity with the specified reset token, or null if not found
     */
    public User findByResetToken(String token) {
        try {
            return em.createQuery("SELECT u FROM User u WHERE u.resetToken = :token", User.class).setParameter("token", token).getSingleResult();
        } catch (jakarta.persistence.NoResultException e) {
            return null;
        }
    }

    
    /**
     * Updates the reset token for a user.
     *
     * @param token the new reset token
     * @param expiration the date and time when the token will expire
     * @param userId the user id for which the token should be updated
     */
    public void updateResetToken(String token, LocalDateTime expiration, Long userId) {
        User user = em.find(User.class, userId);
        user.setResetToken(token);
        user.setResetTokenExpiration(expiration);
        em.merge(user);
    }

    public void updateUser(User user) {
        em.merge(user);
    }
}
