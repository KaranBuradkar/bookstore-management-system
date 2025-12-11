package com.karanburadkar.bookstore.exception.database;

public class AuthorNotFoundException extends EntityNotFoundException {

    private String message;

    public AuthorNotFoundException(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
