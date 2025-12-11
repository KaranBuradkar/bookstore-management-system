package com.karanburadkar.bookstore.services;

import com.karanburadkar.bookstore.entity.RefreshToken;
import org.springframework.stereotype.Service;

@Service
public interface RefreshTokenService {

    RefreshToken createRefreshToken(String username);

    RefreshToken verifyRefreshToken(String refreshToken);
}
