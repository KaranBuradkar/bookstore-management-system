package com.karanburadkar.bookstore.security;

import com.karanburadkar.bookstore.exception.database.BookNotFoundException;
import com.karanburadkar.bookstore.exception.security.UnauthorizedPublisherAccessException;
import com.karanburadkar.bookstore.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service("permissionGuard")
public class PermissionGuard {

    private static final Logger log = LoggerFactory.getLogger(PermissionGuard.class);
    private final CustomerRepository customerRepository;
    private final BookRepository bookRepository;
    private final OrderRepository orderRepository;
    private final ReviewRepository reviewRepository;
    private final PaymentRepository paymentRepository;
    private final PublisherRepository publisherRepository;

    public PermissionGuard(
            CustomerRepository customerRepository,
            BookRepository bookRepository,
            OrderRepository orderRepository,
            ReviewRepository reviewRepository,
            PaymentRepository paymentRepository,
            PublisherRepository publisherRepository
    ) {
        this.customerRepository = customerRepository;
        this.bookRepository = bookRepository;
        this.orderRepository = orderRepository;
        this.reviewRepository = reviewRepository;
        this.paymentRepository = paymentRepository;
        this.publisherRepository = publisherRepository;
    }

    public boolean isOwnerByPaymentId(Long paymentId, Long userId) {
        Long foundUserId = paymentRepository.validateUser(paymentId);
        return checkOwnership("payment", paymentId, userId, foundUserId);
    }

    public boolean isOwnerByReviewId(Long reviewId, Long userId) {
        Long foundUserId = reviewRepository.validateUser(reviewId);
        return checkOwnership("review", reviewId, userId, foundUserId);
    }

    public boolean isOwnerByCustomerId(Long customerId, Long userId) {
        Long foundUserId = customerRepository.validateUser(customerId);
        return checkOwnership("customer", customerId, userId, foundUserId);
    }

    public boolean isOwnerByOrderId(Long orderId, Long userId) {
        Long foundUserId = orderRepository.validateUser(orderId);
        return checkOwnership("order", orderId, userId, foundUserId);
    }

    public boolean isOwnerByBookId(Long bookId, Long userId) {
        Long foundUserId = bookRepository.getUserIdById(bookId)
                .orElseThrow(() -> new BookNotFoundException(bookId));
        return checkOwnership("book", bookId, userId, foundUserId);
    }

    public boolean isOwnerByPublisherId(Long publisherId, Long userId) {
        Long foundUserId = publisherRepository.getUserIdById(publisherId)
                .orElseThrow(() -> new UnauthorizedPublisherAccessException(
                        "Publisher not found: " + publisherId));
        return checkOwnership("publisher", publisherId, userId, foundUserId);
    }

    private boolean checkOwnership(String type, Long entityId, Long userId, Long foundUserId) {
        if (Objects.equals(userId, foundUserId)) {
            log.debug("User {} owns {} {}", userId, type, entityId);
            return true;
        }
        log.warn("Unauthorized access attempt: user id {} -> {} id {}", userId, type, entityId);
        throw new UnauthorizedPublisherAccessException(
                "you are not authorized to access other user's detail"
        );
    }
}
