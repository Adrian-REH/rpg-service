package com.mmo.service.gateway.application;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Service
public class JwtService {
    private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private static final long TOKEN_VALIDITY = 3600_000; // 1 hora

    public static String generateJwt(String playerId) {
        long nowMillis = System.currentTimeMillis();
        long expMillis = nowMillis + TOKEN_VALIDITY; // 1 hora de validez
        Date now = new Date(nowMillis);
        Date exp = new Date(expMillis);

        return Jwts.builder()
                .setSubject(playerId) // aquí puedes poner el playerId como subject
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(key)
                .compact();
    }
    public String generateJwt(String playerId, String username, String email) {
        long nowMillis = System.currentTimeMillis();
        long expMillis = nowMillis + TOKEN_VALIDITY;
        Date now = new Date(nowMillis);
        Date exp = new Date(expMillis);

        return Jwts.builder()
                .setSubject(playerId)
                .claim("username", username)
                .claim("email", email)
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(key)
                .compact();
    }

    public static Key getSigningKey() {
        return key;
    }
    public boolean isValid(String token) {
        return token != null && !token.isEmpty() && !token.equals("INVALID");
    }

    public Collection<? extends GrantedAuthority> getAuthorities(String token) {
        return List.of(new SimpleGrantedAuthority("ROLE_PLAYER"));
    }


    /**
     * Valida el JWT y extrae el playerId
     * @param token JWT token
     * @return playerId (subject del token)
     * @throws io.jsonwebtoken.JwtException si el token es inválido o expirado
     */
    public String getPlayerIdFromToken(String token) {
        Claims claims = getClaims(token);
        return claims.getSubject();
    }

    /**
     * Valida el JWT y extrae todos los claims
     * @param token JWT token
     * @return Claims del token
     * @throws io.jsonwebtoken.JwtException si el token es inválido o expirado
     */
    public Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
