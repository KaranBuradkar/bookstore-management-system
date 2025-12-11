package com.karanburadkar.bookstore.dto;

import jakarta.validation.constraints.NotBlank;

public class UserDto {

    private Long id;
    @NotBlank(message = "Please enter your username or email")
    private String username;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
