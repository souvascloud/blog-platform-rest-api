package com.souvanik.blog.auth.security.service.impl;

import com.souvanik.blog.auth.security.JwtProperties;
import com.souvanik.blog.auth.security.service.JwtService;
import com.souvanik.blog.user.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.List;

/*
 * Copyright (c) 2025 Souvanik Saha
 *
 * Licensed under the MIT License.
 * https://opensource.org/licenses/MIT
 */
@Service
public class JwtServiceImpl implements JwtService {

    private static final Logger logger = LoggerFactory.getLogger(JwtServiceImpl.class);

    private final JwtProperties props;
    private final Key key;

    public final SecureRandom SECURE_RANDOM = new SecureRandom();


    public JwtServiceImpl(JwtProperties props) {
        this.props = props;
        this.key = Keys.hmacShaKeyFor(props.getSecret().getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public String generateAccessToken(User user) {
        Instant now = Instant.now();
        Instant expiry = now.plus(Duration.ofMinutes(props.getAccessTokenExpiryMinutes()));

        logger.debug("Generating access token for userId={}", user.getId());

        return Jwts.builder()
                .setSubject(user.getEmail())
                .setIssuer("auth-service")
                .setAudience("api")
                .claim("uid", user.getId().toString())
                .claim("roles", List.of(user.getRole().name()))
                .setIssuedAt(Date.from(now))
                .setNotBefore(Date.from(now.minusSeconds(5)))
                .setExpiration(Date.from(expiry))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public String extractUsername(String token) {
        return parse(token).getBody().getSubject();
    }

    @Override
    public boolean isTokenValid(String token, User user) {
        String username = extractUsername(token);
        return username.equals(user.getEmail()) && !isExpired(token);
    }

    private boolean isExpired(String token) {
        return parse(token).getBody().getExpiration().before(new Date());
    }

    private Jws<Claims> parse(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
    }

    @Override
    public String generateRefreshToken() {
        byte[] randomBytes = new byte[32]; // 256-bit
        SECURE_RANDOM.nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding()
                .encodeToString(randomBytes);
    }
}