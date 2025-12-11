package com.karanburadkar.bookstore.exception.database;

public class AddressNotFoundException extends EntityNotFoundException {

    private final String message;

    public AddressNotFoundException(Long addressId) {
        this("Address not found with addressId: "+addressId);
    }

    public AddressNotFoundException(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
