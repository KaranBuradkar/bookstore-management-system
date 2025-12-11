package com.karanburadkar.bookstore.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record RefreshTokenResponse(
        @JsonProperty("access_token") String accessToken,
        @JsonProperty("refresh_token") String refreshToken
) {
}
