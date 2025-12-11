package com.karanburadkar.bookstore.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AuthorDto {
    private Long id;

    @NotBlank(message = "Author must have name")
    @Size(max = 100, message = "Author name must be at most 100 characters")
    private String name;

    @NotBlank(message = "Bio cannot be blank")
    @Size(max = 200, message = "Bio must be at most 200 characters")
    private String bio;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }
}
