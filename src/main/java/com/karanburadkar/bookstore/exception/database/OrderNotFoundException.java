package com.karanburadkar.bookstore.exception.database;

public class OrderNotFoundException extends EntityNotFoundException {

    private final String message;

    public OrderNotFoundException(String message) {
        super(message);
        this.message = message;
    }

    public OrderNotFoundException(Long orderId) {
        this("Order not found with orderId: "+orderId);
    }

    @Override
    public String getMessage() {
        return message;
    }
}
