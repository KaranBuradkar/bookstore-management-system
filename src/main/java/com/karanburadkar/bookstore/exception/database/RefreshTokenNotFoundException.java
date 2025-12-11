package com.karanburadkar.bookstore.exception.database;

public class RefreshTokenNotFoundException extends DatabaseException {
    public RefreshTokenNotFoundException(String noRefreshTokenFound, Object data) {
        super(noRefreshTokenFound, data);
    }
}
