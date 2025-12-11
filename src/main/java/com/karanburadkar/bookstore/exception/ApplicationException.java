package com.karanburadkar.bookstore.exception;

public class ApplicationException extends RuntimeException {

    private Object data;

    public ApplicationException(String message, Object data) {
        super(message);
        this.data = data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Object getData() {
        return data;
    }
}
