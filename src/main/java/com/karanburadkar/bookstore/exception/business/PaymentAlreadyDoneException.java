package com.karanburadkar.bookstore.exception.business;

public class PaymentAlreadyDoneException extends BusinessException {
    public PaymentAlreadyDoneException() {
        super("Payment already done");
    }
}
