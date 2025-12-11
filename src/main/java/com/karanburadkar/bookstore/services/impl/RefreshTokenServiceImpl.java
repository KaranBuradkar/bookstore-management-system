package com.karanburadkar.bookstore.services.impl;

import com.karanburadkar.bookstore.entity.RefreshToken;
import com.karanburadkar.bookstore.entity.User;
import com.karanburadkar.bookstore.exception.database.EntityNotFoundException;
import com.karanburadkar.bookstore.exception.database.UserNotFoundException;
import com.karanburadkar.bookstore.repository.RefreshTokenRepository;
import com.karanburadkar.bookstore.repository.UserRepository;
import com.karanburadkar.bookstore.services.RefreshTokenService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private static final Logger log = LoggerFactory.getLogger(RefreshTokenServiceImpl.class);
    private final RefreshTokenRepository refreshTokenRepository;
    private final Long refreshTokenExpiration;
    private final UserRepository userRepository;

    public RefreshTokenServiceImpl(RefreshTokenRepository refreshTokenRepository,
                                   @Value("${application.security.jwt.refresh-token-expiration}") Long refreshTokenExpiration,
                                   UserRepository userRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.refreshTokenExpiration = refreshTokenExpiration;
        this.userRepository = userRepository;
    }

    @Transactional
    public RefreshToken createRefreshToken(String username) {

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setRefreshToken(UUID.randomUUID().toString());
        refreshToken.setExpiry(Instant.now().plusMillis(refreshTokenExpiration));
        refreshToken.setUser(resolveUser(username));

        return refreshTokenRepository.save(refreshToken);
    }

    private User resolveUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.warn("No user found with username {}", username);
                    return new UserNotFoundException("No user found");
                });
    }

    @Override
    public RefreshToken verifyRefreshToken(String refreshToken) {
        RefreshToken refreshToken1 = refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> {
                    log.warn("No refresh token found");
                    return new EntityNotFoundException("No refresh token found");
                });
        if (refreshToken1.getExpiry().compareTo(Instant.now()) < 0) {
            throw new RuntimeException("Refresh token is expired");
        }

        return refreshToken1;
    }
}
