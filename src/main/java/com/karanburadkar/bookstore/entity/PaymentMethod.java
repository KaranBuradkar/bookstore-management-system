package com.karanburadkar.bookstore.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "payment_methods")
public class PaymentMethod {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    public PaymentMethod() {
    }

    public PaymentMethod(String name) {
        this.name = name;
    }

    public static PaymentMethod of(Long id, String name) {
        return new PaymentMethod(id, name);
    }

    public PaymentMethod(Long id, String name) {
        this.id = id;
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
