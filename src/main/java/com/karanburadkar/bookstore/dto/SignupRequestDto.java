package com.karanburadkar.bookstore.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.karanburadkar.bookstore.entity.type.AuthProvider;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class SignupRequestDto {

    @NotBlank(message = "Username required.")
    private String username;

    @NotBlank(message = "Password must be enter")
    private String password;

    @NotBlank(message = "Enter your first name")
    private String firstname;

    @NotBlank(message = "Enter your last name")
    private String lastname;

    @JsonProperty("phone_no")
    @NotBlank(message = "Please enter phone number")
    private String phoneNo;

    @NotNull(message = "Please enter provider")
    private AuthProvider provider;

    public SignupRequestDto() {
    }

    public SignupRequestDto(String username, String password, String phoneNo, AuthProvider provider) {
        this.username = username;
        this.password = password;
        this.phoneNo = phoneNo;
        this.provider = provider;
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

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public AuthProvider getProvider() {
        return provider;
    }

    public void setProvider(AuthProvider provider) {
        this.provider = provider;
    }
}
