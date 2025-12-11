package com.karanburadkar.bookstore.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.karanburadkar.bookstore.entity.type.RoleType;

import java.util.List;

public record LoginResponseDto(
        @JsonProperty("user_id") Long userId,
        @JsonProperty("role_type") List<RoleType> roles,
        @JsonProperty("roles_ids") List<Long> rolesIds,
        @JsonProperty("access_token") String accessToken,
        @JsonProperty("refresh_token") String refreshToken
) {

}
