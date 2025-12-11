package com.karanburadkar.bookstore.exception.business;

public class DuplicateResourceException extends BusinessException {

    public DuplicateResourceException(String message, Object data) {
        super(message, data);
    }
}
