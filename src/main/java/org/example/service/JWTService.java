package org.example.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static io.jsonwebtoken.Jwts.builder;

@Service
public class JWTService {

    @Value("${jwt.secret}")
    private String SECRET;

    public String extractUsername(String token){
        try{
            return extractClaim(token, Claims::getSubject);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        try{
            final Claims claims = extractAllClaims(token);
            return claimsResolver.apply(claims);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Date extractExpiration(String token){
        try{
            return extractClaim(token, Claims::getExpiration);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Boolean isTokenExpired(String token){
        try{
            return extractExpiration(token).before(new Date());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Boolean validateToken(String token, UserDetails userDetails){
        try{
            final String username = extractUsername(token);
            return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String generateToken(String username){
        try{
            Map<String, Object> claims = new HashMap<>();
            return createToken(claims, username);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String createToken(Map<String, Object> claims, String username){
       try{
           long now = System.currentTimeMillis();
           long expirationMillis = 1000 * 60 * 60; // 1 hour
           return builder()
                   .setClaims(claims)
                   .setSubject(username)
                   .setIssuedAt(new Date(now))
                   .setExpiration(new Date(now + expirationMillis))
                   .signWith(getSignKey(), SignatureAlgorithm.HS256)
                   .compact();
       } catch (Exception e) {
           throw new RuntimeException(e);
       }
    }

    private Claims extractAllClaims(String token){
       try{
           return Jwts.parser()
                   .setSigningKey(getSignKey())
                   .build()
                   .parseClaimsJws(token)
                   .getBody();
       } catch (Exception e) {
           throw new RuntimeException(e);
       }
    }

    private Key getSignKey(){
        try{
            byte[] keyBytes = Decoders.BASE64.decode(SECRET);
            return Keys.hmacShaKeyFor(keyBytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
