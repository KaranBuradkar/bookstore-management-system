package com.karanburadkar.bookstore.exception.database;

public class DataIntegrityViolationException extends DatabaseException {
    public DataIntegrityViolationException(String message, Object data) {
        super(message, data);
    }
}
