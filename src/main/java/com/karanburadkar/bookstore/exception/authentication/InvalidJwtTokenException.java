package com.karanburadkar.bookstore.exception.authentication;

public class InvalidJwtTokenException extends AuthException {
    public InvalidJwtTokenException(String message) {
        super(message);
    }
}
