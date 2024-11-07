package com.nisum.nisumapi.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.function.Function;

@Component
@Slf4j
public class JwtService {

    @Value("${application.security.jwt.secret-key}")
    private String secretKeyString;
    @Value("${application.security.jwt.time.expiration}")
    private String timeExpiration;

    private SecretKey secretKey;

    @PostConstruct
    public void init(){
        byte[] decodedKey = Base64.getDecoder().decode(secretKeyString);
        this.secretKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "HmacSHA256");
    }

    public String generateAccessToken(String nameuser){
        return Jwts.builder()
                .subject(nameuser)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + Long.parseLong(timeExpiration)))
                .signWith(secretKey)
                .compact();
    }

    public boolean isTokenValid(String token){
        try {
            Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return true;
        } catch (Exception e){
            log.error("Token invalid, error: ".concat(e.getMessage()));
            return false;
        }
    }

    public String getUsernameFromToken(String token){
        return getClaim(token, Claims::getSubject);
    }

    public <T> T getClaim(String token, Function<Claims, T> claimsFunction){
        Claims claims = extractAllClaims(token);
        return claimsFunction.apply(claims);
    }

    public Claims extractAllClaims(String token) {
        JwtParser parser = Jwts.parser()
                .verifyWith(secretKey)
                .build();
        return parser.parseSignedClaims(token).getPayload();
    }

}
