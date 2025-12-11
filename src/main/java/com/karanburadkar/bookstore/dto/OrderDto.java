package com.karanburadkar.bookstore.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import com.karanburadkar.bookstore.entity.type.ShippingStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class OrderDto {

    private Long id;

    @JsonProperty("customer_id")
    @NotNull(message = "Please enter your customerId")
    private Long customerId;

    @NotNull(message = "Please enter your order date")
    private LocalDate date;

    @JsonProperty("total_amount")
    @NotNull(message = "Please enter your order's total amount")
    private BigDecimal totalAmount;

    @NotNull(message = "Please enter your ShippingStatus")
    private ShippingStatus status;

    @Valid
    @NotNull(message = "Please enter your address to deliver your order.")
    private AddressDto address;

    @Valid
    @JsonProperty("order_items")
    @NotNull(message = "Please enter your order item list.")
    private Set<OrderItemDto> orderItems = new HashSet<>();

    public OrderDto() {
    }

    public OrderDto(Long customerId, LocalDate date,
                    BigDecimal totalAmount, ShippingStatus status,
                    AddressDto address, Set<OrderItemDto> orderItems) {
        this.customerId = customerId;
        this.date = date;
        this.totalAmount = totalAmount;
        this.status = status;
        this.address = address;
        this.orderItems = orderItems;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public ShippingStatus getStatus() {
        return status;
    }

    public void setStatus(ShippingStatus status) {
        this.status = status;
    }

    public AddressDto getAddress() {
        return address;
    }

    public void setAddress(AddressDto address) {
        this.address = address;
    }

    public Set<OrderItemDto> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(Set<OrderItemDto> orderItems) {
        this.orderItems = orderItems;
    }
}
