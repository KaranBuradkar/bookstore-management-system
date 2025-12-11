package com.karanburadkar.bookstore.exception.validation;

public class NullArgumentException extends RuntimeException {
    private final String message;
    public NullArgumentException(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}

