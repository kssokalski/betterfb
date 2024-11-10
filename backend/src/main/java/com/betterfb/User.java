package com.betterfb;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/**
 * Entity class representing a User in the system.
 */
@Entity
public class User {

    // Unique identifier for the User, automatically generated
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Username for the User
    private String username;

    // Password for the User
    private String password;

    // Email address for the User
    private String email;

    /**
     * Gets the unique identifier of the User.
     *
     * @return the User's id
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the User.
     *
     * @param id the User's id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the username of the User.
     *
     * @return the User's username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username of the User.
     *
     * @param username the User's username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the password of the User.
     *
     * @return the User's password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password of the User.
     *
     * @param password the User's password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets the email address of the User.
     *
     * @return the User's email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address of the User.
     *
     * @param email the User's email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }
}

