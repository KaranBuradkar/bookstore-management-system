package com.karanburadkar.bookstore.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CustomerRequestDto {

    @NotBlank(message = "Please enter your name")
    @Size(max = 100, message = "Customer name must be at most 100 characters")
    private String name;

    @NotBlank(message = "Please enter your phone number")
    @Size(max = 10, message = "Customer phone number must be at most 10 characters")
    @JsonProperty("phone_no")
    private String phoneNo;

    public CustomerRequestDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }
}
