package com.karanburadkar.bookstore.exception.security;

public class UnauthorizedPublisherAccessException extends CustomAccessDeniedException {
    public UnauthorizedPublisherAccessException(String msg) {
        super(msg);
    }
}
