package com.grandedev.gestionflotilla.security;
import java.util.Date;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;


@Service
public class JwtService {

    private final SecretKey key;
    private final long expirationTime;

    public JwtService(SecretKeyProperties properties){
        this.expirationTime = properties.getExpirationTime();
        this.key = Keys.hmacShaKeyFor(
                        properties
                        .getSecretKey()
                        .getBytes()
        );
    }

    //Genera un token JWT con el nombre de usuario y el rol como declaraciones
    public String generateToken(String username, String role){
        return Jwts.builder()
                .subject(username) //setSubject() -> Deprecado
                .claim("role", role)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(key)
                .compact();
    }

    //Valida JWT
    public boolean validateToken(String token){
        try{
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    //Extraemos el usuario del JWT
    public String getUserNameFromToken(String token){
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    //Extraemos el rol de JWT
    public String getRoleFromToken(String token){
        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.get("role", String.class);
    }
}
