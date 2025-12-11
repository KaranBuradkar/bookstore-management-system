package com.karanburadkar.bookstore.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PublisherDto {

    private Long id;
    @JsonProperty("user_id")
    private Long userId;
    private String name;
    @JsonProperty("phone_no")
    private String phoneNo;

    public PublisherDto() {
    }

    public PublisherDto(String name) {
        this.name = name;
    }

    public PublisherDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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
