package com.example;

import jakarta.persistence.*;



@Entity // a mark telling the class, that it will be matched on database table
@Table(name = "users") // table name in the database
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //automatic generation for a user primary key
    private int id; //Unique ID of a user

    @Column(nullable = false, unique = true) //new column, can't be null, need to be unique
    private String login; //User login

    @Column(nullable = false)
    private String password; //User password

    @Column(nullable = false)
    private String email; //User email

    @Column(nullable = false)
    private String name; //First name of a user

    @Column
    private String surname; //Last name of a user

     // Getters
     public int getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }
}
