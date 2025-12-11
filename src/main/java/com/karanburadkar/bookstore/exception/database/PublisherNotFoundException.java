package com.karanburadkar.bookstore.exception.database;

public class PublisherNotFoundException extends EntityNotFoundException {

    private final String message;

        public PublisherNotFoundException(Long publisherId) {
        this("Publisher not found with Id: "+publisherId);
    }


    public PublisherNotFoundException(String message) {
        super(message);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
