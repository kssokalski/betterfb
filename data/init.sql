-- init.sql
CREATE DATABASE IF NOT EXISTS betterfb;

USE betterfb;

CREATE TABLE IF NOT EXISTS USER (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) NOT NULL,
    password VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    resettoken VARCHAR(255),  -- Token for resetting password
    resettokenexpiration TIMESTAMP  -- Expiration time for the reset token
);

-- Add any other tables or initialization logic here
