package com.karanburadkar.bookstore.exception.security;

import org.springframework.security.access.AccessDeniedException;

public class CustomAccessDeniedException extends AccessDeniedException {
    public CustomAccessDeniedException(String msg) {
        super(msg);
    }
}
