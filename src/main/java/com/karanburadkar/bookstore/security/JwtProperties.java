package com.karanburadkar.bookstore.security;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "application.security.jwt")
@Validated
public record JwtProperties (
        @NotBlank String secretKey,
        @NotNull Long accessTokenExpiration,
        @NotNull Long refreshTokenExpiration
){}
