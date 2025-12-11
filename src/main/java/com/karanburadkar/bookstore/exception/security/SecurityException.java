package com.karanburadkar.bookstore.exception.security;

import com.karanburadkar.bookstore.exception.ApplicationException;

public class SecurityException extends ApplicationException {
    public SecurityException(String message, Object data) {
        super(message, data);
    }
}
