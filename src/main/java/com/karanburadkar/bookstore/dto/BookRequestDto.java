package com.karanburadkar.bookstore.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

public class BookRequestDto {
    @NotBlank(message = "please enter isbn id")
    @Pattern(
            regexp = "^(?:\\d{9}[\\dXx]|\\d{13})$",
            message = "ISBN must be either 10 digits (last digit may be X) or 13 digits"
    )
    private String isbn;

    @NotBlank(message = "please enter book's title")
    private String title;

    @NotBlank(message = "please enter book's description")
    private String description;

    @NotNull(message = "please enter book's available stock")
    @Min(value = 0, message = "stock cannot be negative")
    private Integer stock;

    @NotNull(message = "please enter book's price")
    @DecimalMin(value = "0.0", inclusive = false, message = "price must be positive")
    private BigDecimal price;

    @JsonProperty("publisher_id")
    @NotNull(message = "please enter book's publisherId")
    private Long publisherId;

    @Valid
    private Set<BookCategoryDto> categories = new HashSet<>();

    @NotNull
    @JsonProperty("author_ids")
    @Size(min = 1, message = "at least one author must be selected")
    private Set<Long> authorIds = new HashSet<>();

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

    public Set<Long> getAuthorIds() {
        return authorIds;
    }

    public void setAuthorIds(Set<Long> authorIds) {
        this.authorIds = authorIds;
    }

    public Long getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(Long publisherId) {
        this.publisherId = publisherId;
    }
}
