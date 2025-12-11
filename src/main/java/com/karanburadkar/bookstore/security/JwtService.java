package com.karanburadkar.bookstore.security;

import com.karanburadkar.bookstore.entity.RefreshToken;
import com.karanburadkar.bookstore.exception.database.RefreshTokenNotFoundException;
import com.karanburadkar.bookstore.repository.RefreshTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import com.karanburadkar.bookstore.entity.User;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

@Component
public class JwtService {

    private static final Logger log = LoggerFactory.getLogger(JwtService.class);

    private final JwtProperties jwtProperties;

    private final RefreshTokenRepository refreshTokenRepository;

    public JwtService(JwtProperties jwtProperties, RefreshTokenRepository refreshTokenRepository) {
        this.jwtProperties = jwtProperties;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    private SecretKey getSecreteKey() {
        return Keys.hmacShaKeyFor(jwtProperties.secretKey().getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(User user) {
        return generateToken(user, jwtProperties.accessTokenExpiration());
    }

    @Transactional
    public String generateNewRefreshToken(User user) {
        String refreshToken = generateToken(user, jwtProperties.refreshTokenExpiration());
        Instant expiry = getExpiry(refreshToken).toInstant();
        RefreshToken saveRefreshToken = refreshTokenRepository.save(
                new RefreshToken(refreshToken, expiry, user)
        );
        return saveRefreshToken.getRefreshToken();
    }

    @Transactional
    public String updateRefreshToken(User user) {
        RefreshToken dbRefreshToken = refreshTokenRepository.findByUser(user)
                .orElseThrow(() -> {
                    log.warn("RefreshToken no found for user id {}", user.getId());
                    return new RefreshTokenNotFoundException("No refresh token found", null);
                });

        String refreshToken = generateToken(user, jwtProperties.refreshTokenExpiration());
        Instant expiry = getExpiry(refreshToken).toInstant();

        dbRefreshToken.setRefreshToken(refreshToken);
        dbRefreshToken.setExpiry(expiry);

        RefreshToken saveRefreshToken = refreshTokenRepository.save(dbRefreshToken);
        return saveRefreshToken.getRefreshToken();
    }

    @Transactional
    public boolean isValidRefreshToken(String refreshToken) {
        boolean exist = refreshTokenRepository.existsByRefreshToken(refreshToken);
        return !isExpiredToken(refreshToken) && exist;
    }

    private String generateToken(User user, Long expireTime) {
        return Jwts.builder()
                .subject(user.getUsername())
                .claim("userId", user.getId().toString())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expireTime))
                .signWith(getSecreteKey())
                .compact();
    }

    public String getUsernameFromToken(String token) {
        return getChaim(token).getSubject();
    }

    public boolean isExpiredToken(String token) {
        try {
            final Date expiration = getExpiry(token);
            return expiration.before(new Date(System.currentTimeMillis()));

        } catch (Exception e) {
            log.warn("token validation error", e);
            return true;
        }
    }

    public Date getExpiry(String token) {
        Claims chaim = getChaim(token);
        return chaim.getExpiration();
    }

    private Claims getChaim(String token) {
        return Jwts.parser()
                .verifyWith(getSecreteKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
