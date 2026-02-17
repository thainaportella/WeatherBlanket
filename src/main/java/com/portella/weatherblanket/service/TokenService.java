package com.portella.weatherblanket.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    public String generateToken(String login) {
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

        return Jwts.builder()
                .setIssuer("temperature-blanket-api")
                .setSubject(login)
                .setIssuedAt(new Date())
                .setExpiration(genExpirationDate())
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    private Date genExpirationDate() {
        return new Date(System.currentTimeMillis() + 2 * 60 * 60 * 1000);
    }

    public String validateToken(String token) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (Exception e) {
            return "";
        }
    }
}
