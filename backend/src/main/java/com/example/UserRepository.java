package com.example;

import java.util.Optional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

//class created to comminucate with the database
public class UserRepository {

    @PersistenceContext //@PersistenceContext allows to make operations with the database using entity manager
    private EntityManager entityManager;

    @Transactional //@Transactional assures, that either all or none of the operations will be executed
    public void save(User user) { //function to save a user in the database
        entityManager.persist(user);
    }

    public Optional<User> findByLogin(String login) { //function to find a user in the database. Optional used to handle possible errors
        return Optional.ofNullable(entityManager.createQuery("SELECT u FROM User u WHERE u.login = :login", User.class)
                .setParameter("login", login)
                .getSingleResult() //asserts that there is only one result
        );
    }
}
