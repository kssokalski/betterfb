package com.example;

import java.security.MessageDigest;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

@RequestScoped //makes the class create a new instance for each request
public class UserService {

    @Inject
    private UserRepository userRepository; //injection of UserRepository to use it in this class

    public String register(String login, String password, String email){ //function made to handle the registration process
        if(userRepository.findByLogin(login).isPresent()){ //checks if login already exists
            return "Login already exists";
        }

        //hashes the password
        String hashedPassword = hashPassword(password);
        
        //creates a new user with the login, hashed password and email
        User user = new User();
        user.setLogin(login);
        user.setPassword(hashedPassword);
        user.setEmail(email);
        userRepository.save(user);
        return "User created";
    }

private String hashPassword(String password) {
    try{
        MessageDigest md = MessageDigest.getInstance("SHA-256"); //creating the instance of the SHA-256 hashing algorithm
        byte[] hash = md.digest(password.getBytes());

        //formatting the hash into a hexadecimal string
        StringBuilder sb = new StringBuilder();
        for (byte b : hash) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString(); //returning the hashed password
    } catch (Exception e) {
        throw new RuntimeException("Cannot hash the password",e);
    }
    }

}
