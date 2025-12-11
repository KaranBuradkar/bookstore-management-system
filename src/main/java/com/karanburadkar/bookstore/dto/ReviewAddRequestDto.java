package com.karanburadkar.bookstore.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ReviewAddRequestDto {

    @NotBlank(message = "Comment must be exist")
    private String comment;

    @NotNull
    @Min(value = 1, message = "Rating must be between 1 to 5")
    @Max(value = 5, message = "Rating must be between 1 to 5")
    private Integer rating;

    @JsonProperty("book_id")
    @NotNull(message = "Book id is required to submit review")
    private Long bookId;

    @JsonProperty("customer_id")
    @NotNull(message = "Customer id is required to submit review")
    private Long customerId;

    public ReviewAddRequestDto() {
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }
}
