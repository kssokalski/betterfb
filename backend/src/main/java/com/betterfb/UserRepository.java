package com.betterfb;

import java.time.LocalDateTime;
import java.util.Optional;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;

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
     * Finds a User entity by its username.
     *
     * @param id the username of the User to be retrieved
     * @return the User entity with the specified username
     */
    public User findById(Long id) {
        try {
            return em.createQuery("SELECT u FROM User u WHERE u.id = :id", User.class).setParameter("id", id).getSingleResult();
        } catch (jakarta.persistence.NoResultException e) {
            return null;
        }
    }

    /**
     * Finds a User entity by its username.
     *
     * @param name the username of the User to be retrieved
     * @return the User entity with the specified username
     */
    public User findByName(String name) {
        try {
            return em.createQuery("SELECT u FROM User u WHERE u.name = :name", User.class).setParameter("name", name).getSingleResult();
        } catch (jakarta.persistence.NoResultException e) {
            return null;
        }
    }

    /**
     * Finds a User entity by its username.
     *
     * @param surname the username of the User to be retrieved
     * @return the User entity with the specified username
     */
    public User findBySurname(String surname) {
        try {
            return em.createQuery("SELECT u FROM User u WHERE u.surname = :surname", User.class).setParameter("surname", surname).getSingleResult();
        } catch (jakarta.persistence.NoResultException e) {
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


    /**
     * Finds all users which match the given search query in their username, name or surname.
     * The search is case-insensitive and the search query is treated as a substring to be found.
     * The user with the id given as the second parameter is excluded from the search results.
     *
     * @param searchQuery the search query to be searched for in the usernames, names and surnames of the users
     * @param loggedInUserId the id of the user which should be excluded from the search results
     * @return a list of users which match the search query
     */
    public List<User> findUsersForFriendSearch(String searchQuery, Long loggedInUserId) {
    String query = "SELECT u FROM User u WHERE u.id != :loggedInUserId AND (u.username LIKE :searchQuery OR u.name LIKE :searchQuery OR u.surname LIKE :searchQuery)";
    return em.createQuery(query, User.class)
             .setParameter("loggedInUserId", loggedInUserId)
             .setParameter("searchQuery", "%" + searchQuery + "%")
             .getResultList();
    }


    /**
     * Updates the given User entity in the database.
     *
     * @param user the User entity to be updated
     */
    public void updateUser(User user) {
        em.merge(user);
    }

    /**
     * Saves a new FriendRequest entity to the database.
     *
     * @param friendRequest the FriendRequest entity to be persisted
     */
    public void saveFriendRequest(FriendRequest friendRequest) {
        em.persist(friendRequest);
    }

    /**
     * Finds a FriendRequest entity by its id.
     *
     * @param requestId the id of the FriendRequest to be retrieved
     * @return the FriendRequest entity with the specified id, or null if not found
     */
    public FriendRequest findFriendRequestById(Long requestId) {
        return em.find(FriendRequest.class, requestId);
    }

    /**
     * Updates the given FriendRequest entity in the database.
     *
     * @param friendRequest the FriendRequest entity to be updated
     */
    public void updateFriendRequest(FriendRequest friendRequest) {
        em.merge(friendRequest);
    }

    
    /**
     * Finds all users which have accepted a friend request from the given user.
     *
     * @param userId the id of the user which sent the friend requests
     * @return a list of users which accepted a friend request from the given user
     */
    public List<User> findFriendsSent(Long userId) {
        String query = "SELECT f.receiver FROM FriendRequest f WHERE f.sender.id = :userId AND f.accepted = true";
        return em.createQuery(query, User.class)
                 .setParameter("userId", userId)
                 .getResultList();
    }
    
    /**
     * Finds all users who have sent and whose friend requests have been accepted by the given user.
     *
     * @param userId the id of the user who received the friend requests
     * @return a list of users who sent and whose friend requests were accepted by the given user
     */

    public List<User> findFriendsReceived(Long userId) {
        String query = "SELECT f.sender FROM FriendRequest f WHERE f.receiver.id = :userId AND f.accepted = true";
        return em.createQuery(query, User.class)
                 .setParameter("userId", userId)
                 .getResultList();
    }
    
    

}
