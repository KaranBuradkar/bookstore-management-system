package com.karanburadkar.bookstore.exception.authentication;

public class UsernameNotFoundException extends AuthException {
    public UsernameNotFoundException(String message) {
        super(message);
    }
}
