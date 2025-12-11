package com.karanburadkar.bookstore.exception.database;

public class CustomerNotFoundException extends EntityNotFoundException {
    private final String message;

    public CustomerNotFoundException(Long customerId) {
        this("Customer not found with customerId: "+customerId);
    }

    public CustomerNotFoundException(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
