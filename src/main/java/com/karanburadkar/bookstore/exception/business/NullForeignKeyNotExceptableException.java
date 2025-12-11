package com.karanburadkar.bookstore.exception.business;

public class NullForeignKeyNotExceptableException extends BusinessException {

    public NullForeignKeyNotExceptableException(String message, Object data) {
        super(message);
    }
}
