package com.karanburadkar.bookstore.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

public class BookResponseDto {

    private Long id;
    private String title;
    private String isbn;
    private String description;
    private BigDecimal price;
    private Integer stock;
    private Set<BookCategoryDto> categories;
    private Set<AuthorDto> authors;
    private PublisherDto publisher;

    public BookResponseDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<BookCategoryDto> getCategories() {
        return categories;
    }

    public void setCategories(Set<BookCategoryDto> categories) {
        this.categories = categories;
    }

    public Set<AuthorDto> getAuthors() {
        return authors;
    }

    public void setAuthors(Set<AuthorDto> authors) {
        this.authors = authors;
    }

    public PublisherDto getPublisher() {
        return publisher;
    }

    public void setPublisher(PublisherDto publisher) {
        this.publisher = publisher;
    }
}
