package app.udala.auth.service;

import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import app.udala.auth.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class TokenService {
    @Value("${ssn.jwt.expiration}")
    private String expiration;

    @Value("${ssn.jwt.secret}")
    private String secret;

    public String generateToken(Authentication auth) {
        User logged = (User) auth.getPrincipal();

        Date now = new Date();
        Date expires = new Date(now.getTime() + Long.parseLong(expiration));

        return Jwts.builder().setIssuer("SSN Login").setSubject(logged.getPublicId().toString()).setExpiration(expires)
                .setIssuedAt(now).signWith(SignatureAlgorithm.HS256, secret).compact();
    }

    public boolean isValidToken(String token) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public UUID getUserPublicId(String token) {
        Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        return UUID.fromString(claims.getSubject());
    }
}
