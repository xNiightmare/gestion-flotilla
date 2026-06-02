package com.grandedev.gestionflotilla.config;
import java.util.Date;

import com.grandedev.gestionflotilla.dto.UsuarioResponseDTO;
import com.grandedev.gestionflotilla.security.SecretKeyProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;


@Service
public class JwtService {

    private final SecretKey key;
    private final long expirationTime;
    private final long refreshTime;

    public JwtService(SecretKeyProperties properties, JwtService jwtService){
        this.expirationTime = properties.getExpirationTime();
        this.refreshTime = properties.getRefreshTime();
        this.key = Keys.hmacShaKeyFor(
                        properties
                        .getSecretKey()
                        .getBytes()
        );
    }

    public String extractUsername(String token){
        return extractClaim(token,Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(UsuarioResponseDTO usuarioResponseDTO){
        return generateToken(new HashMap<>(), usuarioResponseDTO);
    }

    public String generateToken(
            Map<String, Object> extraClaims,
            UsuarioResponseDTO usuarioResponseDTO
    ){
        return buildToken(extraClaims, usuarioResponseDTO, expirationTime);
    }

    public String generateRefreshToken(
            UsuarioResponseDTO usuarioResponseDTO
    ){
        return buildToken(new HashMap<>(),usuarioResponseDTO, refreshTime);
    }

    private String buildToken(
            Map<String,Object> extraClaims,
            UsuarioResponseDTO usuarioResponseDTO,
            long expiration
    ){
        return Jwts
                .builder()
                .claims(extraClaims)
                .subject(usuarioResponseDTO.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key, Jwts.SIG.HS256)
                .compact();
    }

    public boolean isTokenValid(String token, UsuarioResponseDTO usuarioResponseDTO){
        final String username = extractUsername(token);
        return (username.equals(usuarioResponseDTO.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token){
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token){
        return Jwts
                .parser()
                .verifyWith(key) //before setSigningKey()
                .build()
                .parseSignedClaims(token)
                .getPayload();

    }
}
