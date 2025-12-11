package com.karanburadkar.bookstore.dto;

import org.hibernate.validator.constraints.Length;

public class BookCategoryDto {

    private Long id;
    @Length(max = 20, min = 2, message = "category length must be between 2 and 20 letters")
    private String name;

    public BookCategoryDto() {
    }

    public BookCategoryDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public BookCategoryDto(String name) {
        this.name = name;
    }

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
}
