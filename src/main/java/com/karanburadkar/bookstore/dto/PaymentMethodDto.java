package com.karanburadkar.bookstore.dto;

import jakarta.validation.constraints.NotBlank;

public class PaymentMethodDto {

    private Long id;
    @NotBlank(message = "payment method can not be null")
    private String name;

    public PaymentMethodDto() {
    }

    public PaymentMethodDto(String name) {
        this.name = name;
    }

    public PaymentMethodDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static PaymentMethodDto of(Long id, String name) {
        return new PaymentMethodDto(id, name);
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
