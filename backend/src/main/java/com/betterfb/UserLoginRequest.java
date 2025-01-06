/**
 * Represents a request to log in a user.
 */
package com.betterfb;

public class UserLoginRequest {

    /**
     * The username of the user to log in.
     */
    private String username;

    /**
     * The email address of the user to log in.
     */
    private String email;

    /**
     * The password of the user to log in.
     */
    private String password;

    /**
     * Gets the username of the user to log in.
     * 
     * @return the username of the user to log in
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username of the user to log in.
     * 
     * @param username the username of the user to log in
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the email address of the user to log in.
     * 
     * @return the email address of the user to log in
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address of the user to log in.
     * 
     * @param email the email address of the user to log in
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the password of the user to log in.
     * 
     * @return the password of the user to log in
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password of the user to log in.
     * 
     * @param password the password of the user to log in
     */
    public void setPassword(String password) {
        this.password = password;
    }
}

