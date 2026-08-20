package com.demo.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.util.Date;

public class JwtUtils {
    private final Algorithm algorithm;
    private final long expirationMs = 24 * 3600 * 1000; // 24h

    public JwtUtils(String secret) {
        this.algorithm = Algorithm.HMAC256(secret);
    }

    public String generateToken(String username) {
        return JWT.create()
                .withSubject(username)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + expirationMs))
                .sign(algorithm);
    }

    public String getUsernameFromToken(String token) {
        DecodedJWT jwt = JWT.require(algorithm).build().verify(token);
        return jwt.getSubject();
    }

    public boolean validateToken(String token) {
        try {
            JWT.require(algorithm).build().verify(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
