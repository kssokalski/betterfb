-- init.sql
CREATE DATABASE IF NOT EXISTS betterfb;

USE betterfb;

CREATE TABLE IF NOT EXISTS USER (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) NOT NULL,
    name VARCHAR(100) NULL,
    surname VARCHAR(100) NULL,
    password VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    resettoken VARCHAR(255),  -- Token for resetting password
    resettokenexpiration TIMESTAMP  -- Expiration time for the reset token
);

CREATE TABLE IF NOT EXISTS FriendRequest (
    id INT AUTO_INCREMENT PRIMARY KEY,
    sender_id INT NOT NULL,
    receiver_id INT NOT NULL,
    sentTime TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    accepted BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (sender_id) REFERENCES USER(id),
    FOREIGN KEY (receiver_id) REFERENCES USER(id)
);

-- Add any other tables or initialization logic here
