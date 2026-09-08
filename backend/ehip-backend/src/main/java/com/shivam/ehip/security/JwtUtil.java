package com.shivam.ehip.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.security.Key;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;
    //MUST BE ATLEAST 32 CHARACTERS FOR HS256

    public String generateToken(String username, String role) {
        Key key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        try {
            return Jwts.builder()
                    .setSubject(username)
                    .claim("role", role)
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis()+ 86400000))
                    .signWith(key)
                    .compact();
        }catch(Exception e) {
            e.printStackTrace();
            throw e;
        }
    }


    public String extractLoginName(String token) {
        return extractAllClaims(token).getSubject();
    }

    public String extractRole(String token) {
        return extractAllClaims(token).get("role", String.class);    //CUSTOM CLAIM added during token generation
    }

    /*** extractLoginName(token).equals(loginName) --> Checks:Token belongs to this user; 	***/
    public boolean validateToken(String token, String loginName) {
        return extractLoginName(token).equals(loginName) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractAllClaims(token)
                .getExpiration()
                .before(new Date());            // Expiry < current time --> Expired ::: Expiry > current time --> Valid
    }

    private Claims extractAllClaims(String token) {
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));     // Converts your SECRET string → cryptographic key
        // Used to verify token signature

        return Jwts.parserBuilder()                // Creates JWT parser
                .setSigningKey(key)                // Verifies token is not tampered  --- If invalid → throws exception
                .build()
                .parseClaimsJws(token)			   // Splits token into: Header, Payload (Claims), Signature --  Validates signature using key
                .getBody();						   // returns claims
    }
}
