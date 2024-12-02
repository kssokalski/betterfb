package com.betterfb;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.awt.*;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

public class JwtUtility {
    static final Key SECRET_KEY = generateSecretKey();
    private static final long EXPIRATION_TIME = 86400000;


    //Generowanie klucza
    private static Key generateSecretKey() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(SignatureAlgorithm.HS256.getJcaName());
            keyGenerator.init(256); //Ustawiona dlugosc klucza na 256 bitow
            SecretKey secretKey = keyGenerator.generateKey();
            return secretKey;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    // Generowanie tokena JWT
    public static String generateToken(String identifier) {
        return Jwts.builder()
                .setSubject(identifier)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SECRET_KEY)
                .compact();
    }

    // Walidacja tokena JWT
    public static String validateToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (Exception e) {

            return null;
        }
    }

}

