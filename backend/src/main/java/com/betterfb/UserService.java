package com.betterfb;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class UserService {
    private EntityManagerFactory emf;
    private EntityManager em;

    public UserService() {
        emf = Persistence.createEntityManagerFactory("my-persistence-unit");
        em = emf.createEntityManager();
    }

    // Metoda logowania
    public User authenticate(String identifier, String password) {
        try {
            User user = em.createQuery("SELECT u FROM User u WHERE u.username = :identifier OR u.email = :identifier", User.class)
                    .setParameter("identifier", identifier)
                    .getSingleResult();

            if (user.getPassword().equals(password)) {
                return user;
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }
}
