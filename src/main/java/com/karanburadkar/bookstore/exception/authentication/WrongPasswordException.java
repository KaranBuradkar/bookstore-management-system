package com.karanburadkar.bookstore.exception.authentication;

public class WrongPasswordException extends AuthException {
    public WrongPasswordException(String message) {
        super(message);
    }
}
