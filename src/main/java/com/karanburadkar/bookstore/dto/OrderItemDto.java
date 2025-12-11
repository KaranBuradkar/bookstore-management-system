package com.karanburadkar.bookstore.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class OrderItemDto {

    private Long id;

    @JsonProperty("book_id")
    @NotNull(message = "bookId never null")
    private Long bookId;

    @NotNull(message = "quantity never null")
    private Integer quantity;

    @JsonProperty("unit_price")
    @NotNull(message = "unitPrice never null")
    private BigDecimal unitPrice;

    @JsonProperty("sub_total")
    @NotNull(message = "subTotal never null")
    private BigDecimal subTotal;

    public OrderItemDto() {
    }

    public OrderItemDto(Long id, Long bookId, Integer quantity, BigDecimal unitPrice, BigDecimal subTotal) {
        this.id = id;
        this.bookId = bookId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.subTotal = subTotal;
    }

    public Long getId() {
        return id;
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

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(BigDecimal subTotal) {
        this.subTotal = subTotal;
    }
}
