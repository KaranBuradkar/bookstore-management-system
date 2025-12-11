package com.karanburadkar.bookstore.exception.business;

public class OrderPriceMiscalculationException extends BusinessException {
    public OrderPriceMiscalculationException(String message) {
        super(message, null);
    }
}
