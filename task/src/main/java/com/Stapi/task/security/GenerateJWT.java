package com.Stapi.task.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class GenerateJWT {

    public String generateToken(Authentication authentication){

        Date now = new Date();
        Date expiry = new Date(now.getTime() + SecurityConstants.EXPIRY_TIME);

        String token = Jwts.builder()
                .subject(
                        authentication.getName())
                .issuedAt(now)
                .expiration(expiry)
                .signWith(SecurityConstants.KEY)
                .compact();

        return token;
    }
    public Boolean validate(String token){
        try{
            Jwts
                    .parser()
                    .verifyWith(SecurityConstants.KEY)
                    .build()
                    .parseSignedClaims(token);
            return true;
        }
        catch (Exception ex){
            throw new AuthenticationCredentialsNotFoundException("JWT expired or incorrect");
        }
    }

    public String extractUsername(String token){
        Claims claims = extractClaims(token);
        return claims.getSubject();
    }

    public Claims extractClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(SecurityConstants.KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload();

    }


}
