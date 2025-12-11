package com.karanburadkar.bookstore.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonMerge;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.OptBoolean;

import java.util.List;
import java.util.Set;

public class BookShortResponseDto {

    private Long id;
    private String title;
    private String isbn;
    private String description;
    private Double price;
    private Integer stock;
    private Long publisher;
    @JsonProperty("author_ids")
    @JsonMerge(value = OptBoolean.TRUE)
    private List<Long> authorIds;
    @JsonProperty(namespace = "category_names")
    private Set<String> categoryNames;

    public BookShortResponseDto() {
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Set<String> getCategoryNames() {
        return categoryNames;
    }

    public void setCategoryNames(Set<String> categoryNames) {
        this.categoryNames = categoryNames;
    }

    public List<Long> getAuthorIds() {
        return authorIds;
    }

    public void setAuthorIds(List<Long> authorIds) {
        this.authorIds = authorIds;
    }

    public Long getPublisher() {
        return publisher;
    }

    public void setPublisher(Long publisher) {
        this.publisher = publisher;
    }
}
