package com.betterfb;

import java.time.LocalDateTime;

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

    // Token used for password reset
    private String resetToken;

    // Expiration date for the reset token
    private LocalDateTime resetTokenExpiration;

    /**
     * Gets the reset token for the User.
     *
     * @return the User's reset token
     */
    public String getResetToken() {
        return resetToken;
    }

    /**
     * Sets the reset token for the User.
     *
     * @param resetToken the User's reset token to set
     */
    public void setResetToken(String resetToken) {
        this.resetToken = resetToken;
    }

    /**
     * Gets the expiration date for the User's reset token.
     *
     * @return the User's reset token expiration date
     */
    public LocalDateTime getResetTokenExpiration() {
        return resetTokenExpiration;
    }

    /**
     * Sets the expiration date for the User's reset token.
     *
     * @param resetTokenExpiration the User's reset token expiration date to set
     */
    public void setResetTokenExpiration(LocalDateTime resetTokenExpiration) {
        this.resetTokenExpiration = resetTokenExpiration;
    }

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

