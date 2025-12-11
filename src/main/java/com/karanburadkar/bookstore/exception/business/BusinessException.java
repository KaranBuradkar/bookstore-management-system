package com.karanburadkar.bookstore.exception.business;

import com.karanburadkar.bookstore.exception.ApplicationException;

public class BusinessException extends ApplicationException {

    private Object data;

    public BusinessException(String message) {
        this(message, null);
        data = null;
    }

    public BusinessException(String message, Object data) {
        super(message, data);
        this.data = data;
    }

    public Object getData() {
        return data;
    }
}
