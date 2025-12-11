package com.karanburadkar.bookstore.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.karanburadkar.bookstore.entity.type.AuthProvider;

public record SignupResponseDto(
        Boolean success,
        @JsonProperty("user_id") Long userId,
        String username, String roles,
        @JsonProperty("customer_id") Long customerId,
        AuthProvider provider,
        @JsonProperty("access_token") String accessToken,
        @JsonProperty("refresh_token") String refreshToken
) {
}
