package com.karanburadkar.bookstore.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.karanburadkar.bookstore.entity.type.AuthProvider;

public record PublisherSignupResponseDto(
        Boolean status, String roles,
        @JsonProperty("publisher_id") Long publisherId,
        String username, String name, AuthProvider provider,
        @JsonProperty("access_token") String accessToken,
        @JsonProperty("refresh_token") String refreshToken
) {

}
