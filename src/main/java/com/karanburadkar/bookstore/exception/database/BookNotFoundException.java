package com.karanburadkar.bookstore.exception.database;

public class BookNotFoundException extends EntityNotFoundException {

    private String message;

    public BookNotFoundException(Long bookId) {
        this("Book not found with bookId: "+bookId);
    }

    public BookNotFoundException(String message) {
        super(message);
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
