package com.karanburadkar.bookstore.exception.security;

public class UnauthorizedCustomerAccessException extends CustomAccessDeniedException {
    public UnauthorizedCustomerAccessException(String msg) {
        super(msg);
    }
}
