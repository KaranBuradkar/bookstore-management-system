package com.karanburadkar.bookstore.exception.database;

import com.karanburadkar.bookstore.exception.ApplicationException;

import java.util.Map;

public class DatabaseException extends ApplicationException {
    public DatabaseException(String message, Object data) {
        super(message, data);
    }
}
