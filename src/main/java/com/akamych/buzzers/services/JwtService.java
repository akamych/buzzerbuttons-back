package com.akamych.buzzers.services;

import com.akamych.buzzers.entities.User;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

        @Value("${BUZZERS_JWT_SEC}")
        private String secret;

        @Value("${jwt.expiration}")
        private long expiration;

        @Value("${BUZZERS_COOKIE_NAME}")
        private String JWT_TOKEN_COOKIE_NAME;

        private Key getSigningKey() {
            return Keys.hmacShaKeyFor(secret.getBytes());
        }

        public String generateToken(User user) {
            return Jwts.builder()
                    .setSubject(user.getId().toString())
                    .claim("role", user.getRole())
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + expiration))
                    .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                    .compact();
        }

        public String getUserId(String token) {
            return Jwts.parser()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        }

        public boolean validateToken(String token) {
            try {
                Jwts.parser().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
                return true;
            } catch (JwtException e) {
                return false;
            }
    }

    public void setJwtCookie(User user, HttpServletResponse servletResponse) {
        Cookie cookie = new Cookie(JWT_TOKEN_COOKIE_NAME, generateToken(user));

        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60);
        cookie.setSecure(false);

        servletResponse.addCookie(cookie);
    }

    public void deleteJwtCookie (HttpServletResponse servletResponse) {
        Cookie cookie = new Cookie(JWT_TOKEN_COOKIE_NAME, null);

        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        cookie.setSecure(false);

        servletResponse.addCookie(cookie);
    }
}
