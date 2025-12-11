package com.karanburadkar.bookstore.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import com.karanburadkar.bookstore.entity.type.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PaymentDto {
    private Long id;

    @JsonProperty("order_id")
    @NotNull(message = "order must have pay for")
    private Long orderId;

    @NotNull(message = "payment date must be entered")
    private LocalDate date;

    @NotNull(message = "amount must be enter")
    private BigDecimal amount;

    @NotNull(message = "status is required")
    private PaymentStatus status = PaymentStatus.PENDING;

    @Valid
    @JsonProperty("payment_method")
    private PaymentMethodDto paymentMethod;

    public PaymentDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public PaymentMethodDto getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethodDto paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
}
