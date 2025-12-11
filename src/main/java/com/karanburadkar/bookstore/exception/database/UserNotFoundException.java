package com.karanburadkar.bookstore.exception.database;

public class UserNotFoundException extends EntityNotFoundException {

    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException(Long userId) {
        this("User not found with userId: "+userId);
    }
}
