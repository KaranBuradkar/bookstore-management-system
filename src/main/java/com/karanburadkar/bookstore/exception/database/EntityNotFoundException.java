package com.karanburadkar.bookstore.exception.database;

import com.karanburadkar.bookstore.exception.ApplicationException;

public class EntityNotFoundException extends ApplicationException {

    private String message;

    public EntityNotFoundException(String eName, Long eId) {
        this(eName + " not found with Id: " + eId);
    }

    public EntityNotFoundException(String message) {
        super(message, null);
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
