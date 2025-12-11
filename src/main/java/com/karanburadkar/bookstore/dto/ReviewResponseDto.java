package com.karanburadkar.bookstore.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ReviewResponseDto {

    private Long id;

    @JsonProperty("book_id")
    private Long bookId;

    @JsonProperty("customer_id")
    private Long customerId;
    private String comment;
    private Integer rating;

    public Long getId() {
        return id;
    }

    public ReviewResponseDto(Long id, Long bookId, Long customerId, String comment, Integer rating) {
        this.id = id;
        this.bookId = bookId;
        this.customerId = customerId;
        this.comment = comment;
        this.rating = rating;
    }

    public void setId(Long id) {
        this.id = id;
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

    @Override
    public String toString() {
        return "ReviewResponseDto{" +
                "id=" + id +
                ", bookId=" + bookId +
                ", customerId=" + customerId +
                ", comment='" + comment + '\'' +
                ", rating=" + rating +
                '}';
    }
}
