package com.karanburadkar.bookstore.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.karanburadkar.bookstore.entity.type.AuthProvider;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class PublisherSignupRequestDto {
    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Password must be enter")
    private String password;

    @NotBlank(message = "Please enter phone number")
    @JsonProperty("phone_no")
    private String phoneNo;

    @NotBlank(message = "Please enter your publisher's agency name")
    private String name;

    @NotNull(message = "Please enter provider name")
    private AuthProvider provider;

    public PublisherSignupRequestDto() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AuthProvider getProvider() {
        return provider;
    }

    public void setProvider(AuthProvider provider) {
        this.provider = provider;
    }
}
