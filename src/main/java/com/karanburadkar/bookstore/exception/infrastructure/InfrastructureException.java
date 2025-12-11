package com.karanburadkar.bookstore.exception.infrastructure;

import com.karanburadkar.bookstore.exception.ApplicationException;

public class InfrastructureException extends ApplicationException {
    public InfrastructureException(String message, Object data) {
        super(message, data);
    }
}
